package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.param.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.common.core.constant.DeviceTypeConstants;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.DeviceParamEnum;
import com.aurine.cloudx.estate.constant.enums.device.DeviceRegParamEnum;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.ProjectDeviceParamInfo;
import com.aurine.cloudx.estate.entity.ProjectEntityLevelCfg;
import com.aurine.cloudx.estate.entity.SysServiceParamConf;
import com.aurine.cloudx.estate.service.ProjectDeviceAbnormalService;
import com.aurine.cloudx.estate.service.ProjectEntityLevelCfgService;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.ResponseOperateConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.TopicConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.dto.ResponseOperateDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.param.service.AbstractParamServiceByHuawei;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.enums.DeviceManufactureEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.exception.entity.DeviceAbnormalHandleInfo;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.constant.HuaweiServiceEnum;
import com.aurine.cloudx.estate.util.RedisUtil;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>适用于 米立 门禁设备（区口机、梯口机）</p>
 *
 * @author : 王良俊
 * @date : 2021-07-08 10:18:59
 */
@Service
@Slf4j
public class MiliAccessDeviceParamServiceByHuaweiImpl extends AbstractParamServiceByHuawei {

    @Resource
    ProjectDeviceAbnormalService projectDeviceAbnormalService;

    @Override
    public DevicesResultVo multiDeviceParamSetting(ObjectNode paramsNode, List<String> deviceIdList, List<String> serviceIdList) {
        if (paramsNode.isMissingNode() || CollUtil.isEmpty(serviceIdList)) {
            throw new RuntimeException("缺少必要参数无法进行本次设置");
        }
        List<Map.Entry<String, JsonNode>> finalParamList = new ArrayList<>();

        log.info("[设备参数设置-{}] 开始批量设置设备参数，项目ID：{}，本次设备数量：{}台", getLogMark(), ProjectContextHolder.getProjectId(), deviceIdList.size());

        // 这里获取到设备信息对象列表
        List<ProjectDeviceInfo> deviceInfoList = projectDeviceInfoService.list(new QueryWrapper<ProjectDeviceInfo>().lambda()
                .in(ProjectDeviceInfo::getDeviceId, deviceIdList));
        // 这里获取到不允许同步的参数名-在json中删除这些参数
        List<SysServiceParamConf> sysServiceParamConfList = sysServiceParamConfService.list(new QueryWrapper<SysServiceParamConf>()
                .lambda().in(SysServiceParamConf::getServiceId, serviceIdList).eq(SysServiceParamConf::getIsSync, "0"));
        List<String> removeParamList = sysServiceParamConfList.stream().map(SysServiceParamConf::getParamId).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(removeParamList)) {
            removeParamList.forEach(paramName -> {
                // 这里要先获取到这个参数的父节点（因为remove方法限制）
                ObjectNode parentNode = paramsNode.findParent(paramName);
                if (parentNode != null && !parentNode.isMissingNode()) {
                    parentNode.remove(paramName);
                }
            });
        }

        List<String> rebootServiceIdList = sysServiceParamConfService.getRebootServiceIdList();
        List<String> removeServiceIdList = new ArrayList<>();
        paramsNode.fields().forEachRemaining(nodeEntry -> {
            String serviceId = nodeEntry.getKey();
            if (!serviceIdList.contains(serviceId)) {
                // 不在要同步的参数里面就从json中删除这个参数(这里要暂存到删除列表，否者会提示空指针异常)
                removeServiceIdList.add(serviceId);
                rebootServiceIdList.remove(serviceId);
            } else if (rebootServiceIdList.contains(serviceId)) {
                finalParamList.add(nodeEntry);
            }
        });
        paramsNode.remove(removeServiceIdList);
        List<ProjectDeviceParamInfo> deviceParamInfoList = new ArrayList<>();
        // 这里获取到这些设备原有的参数（因为有些参数不允许同步的但是却是必填的->设备那边限制的），这里主要是将要同步的参数放入设备原有参数中
        Set<String> deviceIdSet = deviceInfoList.stream().map(ProjectDeviceInfo::getDeviceId).collect(Collectors.toSet());
        List<ProjectDeviceParamInfo> currentDeviceParamInfoList = projectDeviceParamInfoService.list(new QueryWrapper<ProjectDeviceParamInfo>()
                .lambda().in(ProjectDeviceParamInfo::getDeviceId, deviceIdSet)
                .notIn(ProjectDeviceParamInfo::getServiceId, DeviceParamEnum.getIsNotADeviceParameterServiceID()));
        Map<String, ObjectNode> deviceParamInfoMap = new HashMap<>();
        // 这里把对应设备的参数数据转换成json对象节点存入map中
        currentDeviceParamInfoList.forEach(projectDeviceParamInfo -> {
            try {
                deviceParamInfoMap.put(projectDeviceParamInfo.getDeviceId() + projectDeviceParamInfo.getServiceId(),
                        objectMapper.readValue(projectDeviceParamInfo.getDeviceParam(), ObjectNode.class));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });

        // 存放设置失败服务ID
        Map<String, List<String>> failedMap = new HashMap<>();

        deviceInfoList.forEach(deviceInfo -> {

            if (StringUtil.isNotEmpty(deviceInfo.getDeviceId()) && StringUtil.isNotEmpty(deviceInfo.getThirdpartyCode())) {
                paramsNode.fields().forEachRemaining(nodeEntry -> {
                    // 这里将只剩下同步的参数数据放入到这台设备原本的参数中
                    // 这里获取到这台设备这个参数的所有json数据
                    ObjectNode originParamNode = deviceParamInfoMap.get(deviceInfo.getDeviceId() + nodeEntry.getKey());
                    if (originParamNode != null) {
                        recursivelyJsonTree(originParamNode, (ObjectNode) nodeEntry.getValue());
                        // 判断设备某些参数是否需要根据别的参数进行调整
                        if (!rebootServiceIdList.contains(nodeEntry.getKey())) {
                            ProjectDeviceParamSetResultVo resultVo = sendDeviceParam(deviceInfo.getDeviceId(),
                                    deviceInfo.getThirdpartyCode(), originParamNode, nodeEntry.getKey(), deviceParamInfoList);
                            if (!resultVo.isSuccess()) {
                                saveParamSetResult(failedMap, resultVo);
                            }
                        }
                    }
                });
                if (CollUtil.isNotEmpty(finalParamList)) {
                    ObjectNode finalParamNode = objectMapper.createObjectNode();
                    finalParamList.forEach(nodeEntry -> {
                        ObjectNode originParamNode = deviceParamInfoMap.get(deviceInfo.getDeviceId() + nodeEntry.getKey());
                        if (originParamNode != null) {
                            recursivelyJsonTree(originParamNode, (ObjectNode) nodeEntry.getValue());
                            originParamNode.fields().forEachRemaining(nodeItem -> {
                                finalParamNode.set(nodeItem.getKey(), nodeItem.getValue());
                            });
                        }
                    });

                    List<ProjectDeviceParamSetResultVo> resultVoList = sendDeviceParam(deviceInfo.getDeviceId(), deviceInfo.getThirdpartyCode(),
                            finalParamNode, deviceParamInfoList);

                    resultVoList.forEach(resultVo -> {
                        if (!resultVo.isSuccess()) {
                            saveParamSetResult(failedMap, resultVo);
                        }
                    });
                }
            } else {
                log.error("[设备参数设置-{}] 本台设备缺少设备ID/设备code无法进行参数设置，设备ID：{}，设备code：{}", getLogMark(),
                        deviceInfo.getDeviceId(), deviceInfo.getThirdpartyCode());
            }
        });

        // 这里保存设置成功的设备参数数据
        if (CollUtil.isNotEmpty(deviceParamInfoList)) {
            projectDeviceParamInfoService.saveDeviceParamBatch(deviceParamInfoList);
        }
        return new DevicesResultVo(deviceInfoList.size() - failedMap.size(),
                failedMap.size(), deviceInfoList.size(), new ArrayList<>(failedMap.keySet()));
    }

    @Override
    protected void deviceParamPreHandle(String serviceId, ObjectNode paramObj) {
        // 在最后发送设备参数之前先判断是否需要根据某些参数进行处理
        if (DeviceParamEnum.DEVICE_NO_OBJ.getServiceId().equals(serviceId)) {
            ObjectNode subSectionParent = paramObj.findParent("subSection");
            if (!subSectionParent.isMissingNode()) {
                String key = ProjectContextHolder.getProjectId() + "SubSection";
                String subsection = (String) RedisUtil.get(key);
                if (subsection == null) {
                    ProjectDeviceNoRule projectSubSection = projectEntityLevelCfgService.getProjectSubSection(ProjectContextHolder.getProjectId());
                    subsection = projectSubSection.getSubSection();
                    RedisUtil.set(key, subsection, 60);
                }
                // 不管对不对每次都设置一遍就对了
                subSectionParent.put("subSection", subsection);
            }
        }
    }

    @Override
    public Set<DeviceManufactureEnum> getApplicableDeviceProducts() {
        Set<DeviceManufactureEnum> supportDeviceManufactureEnum = new HashSet<>();
        supportDeviceManufactureEnum.add(DeviceManufactureEnum.MILI_GATE_DEVICE);
        supportDeviceManufactureEnum.add(DeviceManufactureEnum.MILI_LADDER_WAY_DEVICE);
        return supportDeviceManufactureEnum;
    }

    @Override
    public void deviceDataUpdate(String json, ProjectDeviceInfo deviceInfo) {

        try {
            // 获取到整个json的根节点
            JsonNode rootNode = objectMapper.readTree(json);
            // 这里获取到msgId（虽然好像没什么用）
            String msgId = rootNode.findPath("msgId").asText();
            // 第三方设备编码 可通过'_'字符分割出设备sn码
            String thirdPartyCode = rootNode.findPath("devId").asText();
            ArrayNode servicesDataArr = (ArrayNode) rootNode.findPath("servicesData");

            ResponseOperateDTO responseOperateDTO = new ResponseOperateDTO();
            responseOperateDTO.setCompany(PlatformEnum.HUAWEI_MIDDLE.value);
            JsonNode serviceId = rootNode.findPath("serviceId");

            checkAbnormal(rootNode, deviceInfo);

            // 设备信息和参数不一样所以单独取出来处理
            if (serviceId.asText().equals(HuaweiServiceEnum.DEVICE_INFO.code)) {
                /*
                 * {
                 *     "onNotifyData":{
                 *         "devId":"5fe40e5e5c886202ce5ff5a8_3G2H0000032T",
                 *         "gatewayId":"5fe40e5e5c886202ce5ff5a8_3G2H0000032T",
                 *         "servicesData":[
                 *             {
                 *                 "data":{
                 *                     "devId":"5fe40e5e5c886202ce5ff5a8_3G2H0000032T",
                 *                     "devSN":"3G2H0000032T",
                 *                     "devMac":"66:1b:2e:32:e8:45",
                 *                     "capabilities":"#card#face#yuntalk#yuntel#yunMonitor#textNotice#richTextNotice#mediaAdvert",
                 *                     "productId":"3T11T3C01",
                 *                     "modelId":"3T11T3C01",
                 *                     "gisInfo":"5",
                 *                     "devResType":"djWallDoor",
                 *                     "devName":"5fe40e5e5c886202ce5ff5a8_3G2H0000032T",
                 *                     "manufacturer":"mili"
                 *                 },
                 *                 "eventTime":"1621562834",
                 *                 "serviceId":"DeviceInfo"
                 *             }
                 *         ]
                 *     },
                 *     "resource":"device.data",
                 *     "event":"update",
                 *     "subscriptionId":"7"
                 * }
                 *
                 * */
                // 这里直接获取到设备资源类型
                JsonNode devResType = rootNode.findPath("devResType");
                JsonNode devMac = rootNode.findPath("devMac");
                JsonNode capabilities = rootNode.findPath("capabilities");
                JsonNode gisInfo = rootNode.findPath("gisInfo");
                // 获取到设备信息的设备资源类型并更新设备信息表
                projectDeviceInfoService.update(new LambdaUpdateWrapper<ProjectDeviceInfo>()
                        .set(!devResType.isMissingNode(), ProjectDeviceInfo::getResType, devResType.asText())
                        .set(!devMac.isMissingNode(), ProjectDeviceInfo::getMac, devMac.asText().replaceAll(":", "").toUpperCase())
                        .set(!capabilities.isMissingNode(), ProjectDeviceInfo::getDeviceCapabilities, capabilities.asText())
                        .eq(ProjectDeviceInfo::getThirdpartyCode, thirdPartyCode));

            } else if (serviceId.asText().equals(HuaweiServiceEnum.DEVICE_STATE.code)) {
                // 这里是保存设备状态信息（虽然也是保存在设备参数信息表但是他不属于设备参数）
                /**
                 * {
                 *     "onNotifyData":{
                 *         "devId":"5fe40e5e5c886202ce5ff5a8_3G2H0000032T",
                 *         "gatewayId":"5fe40e5e5c886202ce5ff5a8_3G2H0000032T",
                 *         "servicesData":[
                 *             {
                 *                 "data":{
                 *                     "doorState":0,
                 *                     "cloudCallState":0,
                 *                     "cloudPhoneState":0
                 *                 },
                 *                 "eventTime":"1623892815",
                 *                 "serviceId":"DeviceStateChange"
                 *             }
                 *         ]
                 *     },
                 *     "resource":"device.data",
                 *     "event":"update",
                 *     "subscriptionId":"7"
                 * }
                 * */
                JsonNode data = rootNode.findPath("data");
                ObjectNode objectNode = objectMapper.createObjectNode();
                objectNode.set(DeviceParamEnum.DEVICE_STATE_CHANGE.serviceId, data);

                responseOperateDTO.setDeviceParamJsonVo(new DeviceParamJsonVo(DeviceParamEnum.DEVICE_STATE_CHANGE.serviceId, objectNode.toString()));
                responseOperateDTO.setAction(ResponseOperateConstant.ACTION_UPDATE_DEVICE_STATUS);
                responseOperateDTO.setThirdPartyCode(thirdPartyCode);
                log.info("发送设备额外状态更新：{}", responseOperateDTO.getDeviceParamJsonVo());
                // 再次发送消息
                sendMsg(TopicConstant.SDI_RESPONSE_OPERATE, responseOperateDTO);

            } else {
                // 这里对回调的参数json数据进行解析
                servicesDataArr.forEach(item -> {
                    JsonNode data = item.path("data");
                    if (!data.isMissingNode()) {
                        data.fields().forEachRemaining(keyValue -> {
                            if (keyValue.getValue() instanceof ObjectNode) {
                                // 这里获取到设备参数类型 比如volumeParam（在json中作为存储对应参数的key）
                                String paramKey = keyValue.getKey();
                                // 这里获取到设备参数数据
                                ObjectNode paramData = (ObjectNode) keyValue.getValue();

                                responseOperateDTO.setAction(ResponseOperateConstant.ACTION_UPDATE_DEVICE_PARAM_BASIC);
                                // 设置第三方设备编码
                                responseOperateDTO.setThirdPartyCode(thirdPartyCode);
                                if (DeviceParamEnum.NETWORK_OBJ.getServiceId().equals(paramKey)) {
                                    // 如果是网络参数设置IP地址
                                    deviceInfo.setIpv4(paramData.findPath("ipAddr").asText());
                                } else if (DeviceParamEnum.DEVICE_NO_OBJ.getServiceId().equals(paramKey)) {

                                    if (RedisUtil.hasKey(thirdPartyCode + DeviceParamEnum.DEVICE_NO_OBJ.serviceId)) {
                                        return;
                                    }
                                    // 存入redis 如果30秒内再次接收到这个参数就不处理
                                    RedisUtil.set(thirdPartyCode + DeviceParamEnum.DEVICE_NO_OBJ.serviceId, "1", 30);

                                    // 如果是设备编号参数 这里获取到设备的框架号 (这里更新行为)
                                    String deviceNo = paramData.findPath("deviceNo").asText();
                                    deviceInfo.setDeviceCode(deviceNo);
                                    responseOperateDTO.setDeviceFrameNo(deviceNo);
                                    // 设备编号未达到设备编号规则要求的默认在前面补零，感觉这里没必要判断是否是区口机
                                    if (StrUtil.isNotEmpty(deviceNo)) {
                                        String roomNoLen = paramData.findPath("roomNoLen").asText();
                                        String stairNoLen = paramData.findPath("stairNoLen").asText();

                                        int sum = Integer.sum(Integer.parseInt(roomNoLen), Integer.parseInt(stairNoLen) + 1);
                                        if (sum - deviceNo.length() > 0) {
                                            StringBuilder deviceNoBuilder = new StringBuilder();
                                            for (int i = 0; i < sum - deviceNo.length(); i++) {
                                                deviceNoBuilder.append("0");
                                            }
                                            deviceNoBuilder.append(deviceNo);
                                            paramData.findParent("deviceNo").put("deviceNo", deviceNoBuilder.toString());
                                        } else {
                                            paramData.findParent("deviceNo").put("deviceNo", deviceNo.substring(deviceNo.length() - sum));
                                        }

                                    }
                                    // 这里更新设备相关楼栋的楼栋编号
                                    try {
//                                    projectBuildingInfoService.addThirdCode(deviceInfo, deviceNo);
                                        ProjectContextHolder.setProjectId(deviceInfo.getProjectId());
                                        String projectSubDesc = projectEntityLevelCfgService.getProjectSubDesc(deviceInfo.getProjectId());
                                        // 用来判断是否有工具项目设置修改设备编号参数，如果有则将调整完的参数重新设置给设备
                                        boolean changed = false;

                                        JsonNode devSubDesc = paramData.findPath("devSubDesc");
                                        // 对比项目分段描述
                                        if (!devSubDesc.isMissingNode() && StrUtil.isNotEmpty(projectSubDesc) && !projectSubDesc.equals(devSubDesc.toString())) {
                                            paramData.set("devSubDesc", objectMapper.readTree(projectSubDesc));
                                            changed = true;
                                        }
                                        // 是否启用单元号（项目必须要有单元号，所以设备的是否启用单元号必须为启用）
                                        JsonNode useCellNo = paramData.findPath("useCellNo");
                                        // 如果设备没有启用单元号则设置为启用
                                        if (!useCellNo.isMissingNode() && "0".equals(useCellNo.asText())) {
                                            ObjectNode fatherNode = paramData.findParent("useCellNo");
                                            fatherNode.put("useCellNo", "1");
                                            changed = true;
                                        }
                                        if (changed) {
                                            ObjectNode objectNode = objectMapper.createObjectNode();
                                            objectNode.set(paramKey, paramData);
                                            projectDeviceInfoService.setDeviceParam(objectNode, deviceInfo.getDeviceId());
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                responseOperateDTO.setDeviceInfo(deviceInfo);
                                // 这里先发送消息进行框架或设备基础参数更新
                                sendMsg(TopicConstant.SDI_RESPONSE_OPERATE, responseOperateDTO);
                                // 如果设备没有这个参数则不保存
                                if (StringUtil.isNotEmpty(paramKey)) {
                                    // 这里把参数的json数据放入dto中(这里把paramKey转换成serviceId)
                                    responseOperateDTO.setDeviceParamJsonVo(new DeviceParamJsonVo(paramKey, paramData.toString()));
                                    // 设置行为未修改其他参数
                                    responseOperateDTO.setAction(ResponseOperateConstant.ACTION_UPDATE_DEVICE_PARAM_OTHER);
                                    log.info("发送参数数据：{}", responseOperateDTO.getDeviceParamJsonVo());
                                    // 再次发送消息
                                    sendMsg(TopicConstant.SDI_RESPONSE_OPERATE, responseOperateDTO);
                                }
                            }
                        });
                    }
                });
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void checkAbnormal(JsonNode rootNode, ProjectDeviceInfo deviceInfo) {
        try {
            DeviceAbnormalHandleInfo deviceAbnormalHandleInfo = new DeviceAbnormalHandleInfo();
            deviceAbnormalHandleInfo.setSn(deviceInfo.getSn());
            deviceAbnormalHandleInfo.setDeviceId(deviceInfo.getDeviceId());
            deviceAbnormalHandleInfo.setThirdpartyCode(deviceInfo.getThirdpartyCode());
            JsonNode deviceNo = rootNode.findPath("deviceNo");
            JsonNode ipAddr = rootNode.findPath("ipAddr");
            JsonNode mac = rootNode.findPath("mac");
            if (!deviceNo.isMissingNode()) {
                deviceAbnormalHandleInfo.addParam(DeviceRegParamEnum.DEVICE_NO, deviceNo.asText());
            }
            if (!ipAddr.isMissingNode()) {
                deviceAbnormalHandleInfo.addParam(DeviceRegParamEnum.IPV4, ipAddr.asText());
            }
            if (!mac.isMissingNode()) {
                deviceAbnormalHandleInfo.addParam(DeviceRegParamEnum.MAC, mac.asText());
            }
            projectDeviceAbnormalService.checkAbnormal(deviceAbnormalHandleInfo);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected List<DeviceParamForm> adjustFormData(List<DeviceParamForm> paramFormList, String deviceId) {
        String deviceNoPre = projectDeviceInfoService.getDeviceNoPreByDeviceId(deviceId);

        ProjectEntityLevelCfg houseCfg = projectEntityLevelCfgService.getOne(new QueryWrapper<ProjectEntityLevelCfg>().lambda().eq(ProjectEntityLevelCfg::getLevel, "1").eq(ProjectEntityLevelCfg::getProjectId, ProjectContextHolder.getProjectId()));
        ProjectDeviceInfo deviceInfo = projectDeviceInfoService.getOne(new QueryWrapper<ProjectDeviceInfo>().lambda().eq(ProjectDeviceInfo::getDeviceId, deviceId));
        // 获取项目分段参数
        ProjectDeviceNoRule deviceNoRule = projectEntityLevelCfgService.getProjectSubSection(ProjectContextHolder.getProjectId());
        StringBuilder deviceNoReg;
        StringBuilder deviceNoErrorTip;
        // 这里设备如果是区口机，前面数据库查出来的框架编号会是null
        boolean isUnitDoor = DeviceTypeConstants.LADDER_WAY_DEVICE.equals(deviceInfo.getDeviceType());
        if (isUnitDoor) {
            deviceNoReg = new StringBuilder("^(" + deviceNoPre);
            deviceNoErrorTip = new StringBuilder("异常：设备编号未正确设置，请根据 ").append(deviceNoPre);
            if (StrUtil.isEmpty(deviceInfo.getHouseId()) && houseCfg != null) {
                String num = String.format("%0" + (houseCfg.getCodeRule() - 1) + "d", 0);
                deviceNoReg.append(num);
                deviceNoErrorTip.append(num);
            }
            deviceNoErrorTip.append("+2位分机号（01-99）进行调整!");
            deviceNoReg.append("([1-9]0|0[1-9]|[1-9]{2}))$");
        } else {
            int deviceNoLen = deviceNoRule.getStairNoLen() + deviceNoRule.getRoomNoLen() - 1;
            deviceNoReg = new StringBuilder("^[0]{").append(deviceNoLen).append("}([1-9]0|0[1-9]|[1-9]{2})$");
            deviceNoErrorTip = new StringBuilder("设备编号必须为").append(deviceNoLen + 2).append("位，").append("且区口机设备编号只有最后两位有效前面请补0");
        }

        if (CollUtil.isNotEmpty(paramFormList)) {
            paramFormList.forEach(deviceParamForm -> {
                if (DeviceParamEnum.DEVICE_NO_OBJ.getServiceId().equals(deviceParamForm.getServiceId())) {
                    List<DeviceParamForm.FormItem> formItemList = CollUtil.isEmpty(deviceParamForm.getFormItemList()) ? new ArrayList<>() : deviceParamForm.getFormItemList();
                    formItemList.forEach(formItem -> {
                        String paramId = formItem.getParamId();
                        if (StrUtil.isNotEmpty(deviceNoReg) && "deviceNo".equals(paramId)) {
                            if (isUnitDoor) {
                                formItem.setException(deviceNoReg.toString(), deviceNoErrorTip.toString());
                            } else {
                                formItem.setFormValid(deviceNoReg.toString(), deviceNoErrorTip.toString());
                            }
                        } else if ("devNoRule".equals(paramId) && CollUtil.isNotEmpty(formItem.getFormItemList())) {
                            formItem.getFormItemList().forEach(childFormItem -> {
                                switch (childFormItem.getParamId()) {
                                    case "stairNoLen": // 梯口号长度参数
                                        childFormItem.setFormValid(deviceNoRule.getStairNoLenReg(), "根据项目配置梯口号长度只能设置为 " + deviceNoRule.getStairNoLen());
                                        break;
                                    case "roomNoLen": // 房号长度参数
                                        childFormItem.setFormValid(deviceNoRule.getRoomNoLenReg(), "根据项目配置房号长度只能为设置为 " + deviceNoRule.getRoomNoLen());
                                        break;
                                    case "cellNoLen": // 单元号长度参数
                                        childFormItem.setFormValid(deviceNoRule.getCellNoLenReg(), "根据项目配置单元号长度只能设置为 " + deviceNoRule.getCellNoLen());
                                        break;
                                    case "subSection": // 设备分段参数
                                        childFormItem.setException(deviceNoRule.getSubSectionReg(), "异常：项目分段参数为 " + deviceNoRule.getSubSection() + "，需要设置正确的梯口号、房号、单元号长度");
                                        break;
                                    default:
                                }
                            });
                        }
                    });
                }
            });
        }
        return paramFormList;
    }

    @Override
    protected String getLogMark() {
        return "米立门禁设备";
    }
}
