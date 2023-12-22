package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.callback.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.DeviceParamEnum;
import com.aurine.cloudx.estate.constant.enums.CertmediaTypeEnum;
import com.aurine.cloudx.estate.constant.enums.FrameLevelEnum;
import com.aurine.cloudx.estate.constant.enums.PassRightCertDownloadStatusEnum;
import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.dto.ProjectDevicePassQRDTO;
import com.aurine.cloudx.estate.entity.ProjectDeviceCallEvent;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.ProjectFrameInfo;
import com.aurine.cloudx.estate.entity.ProjectRightDevice;
import com.aurine.cloudx.estate.mapper.ProjectRightDeviceMapper;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.thirdparty.business.entity.constant.ThirdPartyBusinessPlatformEnum;
import com.aurine.cloudx.estate.thirdparty.main.consumer.EventDeviceNoticeConsumer;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.*;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.dto.EventDeviceGatePassDTO;
import com.aurine.cloudx.estate.thirdparty.main.entity.dto.EventDeviceNoticeDTO;
import com.aurine.cloudx.estate.thirdparty.main.entity.dto.EventWarningErrorDTO;
import com.aurine.cloudx.estate.thirdparty.main.entity.dto.ResponseOperateDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.callback.HuaweiCallbackService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.constant.*;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.dto.HuaweiRequestObject;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.vo.event.OpendoorEventObj;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.vo.event.RecordEventObj;
import com.aurine.cloudx.estate.thirdparty.module.wr20.callback.factory.WR20CallbackFactory;
import com.aurine.cloudx.estate.thirdparty.module.wr20.config.WR20ConfigProperties;
import com.aurine.cloudx.estate.thirdparty.module.wr20.constant.WR20ActionConstant;
import com.aurine.cloudx.estate.thirdparty.module.wr20.constant.WR20ServiceEnum;
import com.aurine.cloudx.estate.util.ObjectMapperUtil;
import com.aurine.cloudx.estate.util.RedisUtil;
import com.aurine.cloudx.estate.vo.DeviceParamJsonVo;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoProxyVo;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 对接实现，用于拼接对接参数等逻辑
 *
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-03
 * @Copyright:
 */
@Slf4j
public class HuaweiCallbackServiceImplV1_BACK implements HuaweiCallbackService {

    private static ObjectMapper objectMapper = ObjectMapperUtil.instance();


    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;
    @Resource
    private WR20ConfigProperties wr20ConfigProperties;
    @Resource
    private ProjectDeviceInfoProxyService projectDeviceInfoProxyService;
    @Resource
    private ProjectRightDeviceMapper projectRightDeviceMapper;
    @Resource
    private ProjectRightDeviceService projectRightDeviceService;
    @Resource
    private ProjectDeviceParamInfoService projectDeviceParamInfoService;
    @Resource
    private ProjectBuildingInfoService projectBuildingInfoService;
    @Resource
    private ProjectEntityLevelCfgService projectEntityLevelCfgService;
    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;
    /*@Resource
    private EventDeviceNoticeConsumer projectBuildingInfoService;*/
    @Resource
    private ProjectFrameInfoService projectFrameInfoService;

    /**
     * 获取版本
     *
     * @return
     */
    @Override
    public String getVersion() {
        return VersionEnum.V1.code;
    }


    /**
     * 设备激活通知
     *
     * @param requestJson
     * @return
     */
    @Override
    public void deviceActive(JSONObject requestJson) {
        /**
         * TODO: 设备事件 -  设备激活通知 数据清洗
         */
        EventDeviceNoticeDTO eventDeviceNoticeDTO = new EventDeviceNoticeDTO();
        eventDeviceNoticeDTO.setAction("");
        eventDeviceNoticeDTO.setCompany(PlatformEnum.HUAWEI_MIDDLE.name());
        sendMsg(TopicConstant.SDI_EVENT_DEVICE_NOTICE, requestJson);
    }

    /**
     * 设备状态变化通知 数据清洗
     *
     * @param requestJson
     */
    @Override
    public void deviceStatusUpdate(JSONObject requestJson) {

        String subscriptionId = requestJson.getString("subscriptionId");
        String deviceId = requestJson.getJSONObject("onNotifyData").getString("devId");
        String status = requestJson.getJSONObject("onNotifyData").getString("status");


        EventDeviceNoticeDTO noticeDTO = new EventDeviceNoticeDTO();
        noticeDTO.setAction(EventDeviceNoticeConstant.ACTION_DEVICE_STATUS_UPDATE);
        noticeDTO.setCompany(PlatformEnum.HUAWEI_MIDDLE.name());
        noticeDTO.setStatus(HuaweiOnlineStatusEnum.getByCode(status).cloudCode);//转换为云系统字典值
        noticeDTO.setThirdPartyCode(deviceId);

        sendMsg(TopicConstant.SDI_EVENT_DEVICE_NOTICE, noticeDTO);
    }

    /**
     * 设备属性变化通知 数据清洗
     * 调用 设备事件通知 主题
     *
     * @param requestJson
     */
    @Override
    public void deviceDataUpdate(JSONObject requestJson) {
        String json = requestJson.toJSONString();
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
                        .set(!devMac.isMissingNode(), ProjectDeviceInfo::getMac, devMac.asText().replaceAll(":","").toUpperCase())
                        .set(!capabilities.isMissingNode(), ProjectDeviceInfo::getDeviceCapabilities, capabilities.asText())
                        .eq(ProjectDeviceInfo::getThirdpartyCode, thirdPartyCode));

            } else if(serviceId.asText().equals(HuaweiServiceEnum.DEVICE_STATE.code)) {
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
                responseOperateDTO.setDeviceParamJsonVo(new DeviceParamJsonVo(DeviceParamEnum.DEVICE_STATE_CHANGE.serviceId, data.toString()));
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
                                    ProjectDeviceInfo deviceInfo = new ProjectDeviceInfo();
                                    deviceInfo.setIpv4(paramData.findPath("ipAddr").asText());
                                    responseOperateDTO.setDeviceInfo(deviceInfo);
                                }
                           /* else if (DeviceParamEnum.VERSION_OBJ.getParamsTypeId().equals(paramKey)) {
                                // 如果是版本参数则设置对应的软件版本和硬件版本
                                ProjectDeviceInfo deviceInfo = new ProjectDeviceInfo();
                                deviceInfo.setSoftVersion(paramData.findPath("softwareVer").asText());
                                deviceInfo.setHardVersion(paramData.findPath("hardwareVer").asText());
                                responseOperateDTO.setDeviceInfo(deviceInfo);
                            }*/
                                else if (DeviceParamEnum.DEVICE_NO_OBJ.getServiceId().equals(paramKey)) {
                                    // 如果是设备编号参数 这里获取到设备的框架号 (这里更新行为)
                                    responseOperateDTO.setAction(ResponseOperateConstant.ACTION_UPDATE_DEVICE_PARAM_FRAME);
                                    String deviceNo = paramData.findPath("deviceNo").asText();
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
                                    ProjectDeviceInfo deviceInfo = projectDeviceInfoProxyService
                                            .getByThirdPartyCode(thirdPartyCode);
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

    /**
     * 设备对象管理信息回应 数据清洗
     * 调用 设备事件通知 主题
     *
     * @param requestJson
     */
    @Override
    public void deviceObjManagerReport(JSONObject requestJson) {

        /**
         * 人脸下发返回结果
         * {
         *     "onNotifyData": {
         *         "changeTime": "1606459052",
         *         "devId": "5ed9dc514b495808c82eca87_PEOQL20M08029761S9DJ",
         *         "gatewayId": "5ed9dc514b495808c82eca87_PEOQL20M08029761S9DJ",
         *         "msgId": "01131f916f8b4869aaa",
         *         "objManagerData": {
         *             "action": "ADD",
         *             "objInfo": {
         *                 "passNo": "ebb6e107220d3b30cd9acbc89bf27ae7"
         *             },
         *             "objName": "objName",
         *             "serviceId": "FaceManager"
         *         },
         *         "productId": "5"
         *     },
         *     "resource": "device.objmanager",
         *     "event": "report",
         *     "subscriptionId": "21"
         * }
         *
         */
        //init
        JSONObject onNotifyDataJson = requestJson.getJSONObject("onNotifyData");
        String msgId = onNotifyDataJson.getString("msgId");
        String thirdPartyCode = onNotifyDataJson.getString("devId");
        String gatewayId = onNotifyDataJson.getString("gatewayId");
        JSONObject objManagerDataJson = onNotifyDataJson.getJSONObject("objManagerData");

        String serviceId = objManagerDataJson.getString("serviceId");

//        String result = "1";// 返回结果

        String objName = objManagerDataJson.getString("objName");
        String action = objManagerDataJson.getString("action");

        JSONObject objInfo = null;
        String passNo = "";

        List<ProjectRightDevice> rightDeviceList = null;

        /**
         *  判断处理方法，如果是WR20,直接回调WR20的业务
         */
        Object requestRedisObjStr = RedisUtil.get(HuaweiCacheConstant.HUAWEI_REQUEST_PER + msgId);
        HuaweiRequestObject huaweiRequestObject = null;
        if (requestRedisObjStr != null) {
            huaweiRequestObject = JSONObject.parseObject(requestRedisObjStr.toString(), HuaweiRequestObject.class);
//            ProjectContextHolder.setProjectId(huaweiRequestObject.getProjectId());
        }


        //请求回调数据
        if (huaweiRequestObject != null) {

            if (huaweiRequestObject != null && ThirdPartyBusinessPlatformEnum.WR20.code.equals(huaweiRequestObject.getSource())) {
                //WR20回调数据
                wr20RequestCallbackHandle(serviceId, action, huaweiRequestObject, objManagerDataJson, gatewayId, requestJson);
            } else {
                //直连设备
                huaweiCallbackHandle(serviceId, thirdPartyCode, huaweiRequestObject, objManagerDataJson, requestJson);
            }

        } else {
            //主动上报数据

            //根据service和action，分发处理业务
            switch (WR20ServiceEnum.getByCode(serviceId)) {

                case TENEMENT_MANAGER:
                    switch (action) {
                        case WR20ActionConstant.ADD:
                            log.info("[华为中台] 获取到 [WR20] 添加住户上报事件:{}", requestJson);
                            WR20CallbackFactory.getFactoryInstance().getPersonService(VersionEnum.V1.code).addPersonByWR20(objManagerDataJson.getJSONObject("objInfo"), gatewayId);
                            break;
                        case WR20ActionConstant.UPDATE:
                            log.info("[华为中台] 获取到 [WR20] 更新住户上报事件:{}", requestJson);
                            WR20CallbackFactory.getFactoryInstance().getPersonService(VersionEnum.V1.code).updatePersonByWR20(objManagerDataJson.getJSONObject("objInfo"), gatewayId);
                            break;
                        case WR20ActionConstant.DELETE:
                            log.info("[华为中台] 获取到 [WR20] 删除住户上报事件:{}", requestJson);
                            WR20CallbackFactory.getFactoryInstance().getPersonService(VersionEnum.V1.code).delPersonByWR20(objManagerDataJson.getJSONObject("objInfo"), gatewayId);
                            break;
                    }
                    break;
                case WORKER_MANAGER:
                    switch (action) {
//                        case WR20ActionConstant.GET:
//                            WR20CallbackFactory.getFactoryInstance().getPersonService(VersionEnum.V1.code).syncStaff(objManagerDataJson.getJSONObject("objInfo"), gatewayId);
//                            break;
                        case WR20ActionConstant.ADD:
                            log.info("[华为中台] 获取到 [WR20] 添加员工上报事件:{}", requestJson);
                            WR20CallbackFactory.getFactoryInstance().getPersonService(VersionEnum.V1.code).addStaffByWR20(objManagerDataJson.getJSONObject("objInfo"), gatewayId);
                            break;
                        case WR20ActionConstant.UPDATE:
                            log.info("[华为中台] 获取到 [WR20] 更新员工上报事件:{}", requestJson);
                            WR20CallbackFactory.getFactoryInstance().getPersonService(VersionEnum.V1.code).updateStaffByWR20(objManagerDataJson.getJSONObject("objInfo"), gatewayId);
                            break;
                        case WR20ActionConstant.DELETE:
                            log.info("[华为中台] 获取到 [WR20] 删除员工上报事件:{}", requestJson);
                            WR20CallbackFactory.getFactoryInstance().getPersonService(VersionEnum.V1.code).delStaffByWR20(objManagerDataJson.getJSONObject("objInfo"), gatewayId);
                            break;
                    }
                    break;
                default:
                    log.error("[华为中台] 获取到未定义的 对象信息 事件上报：{}", requestJson);


            }

        }

    }

    private void wr20RequestCallbackHandle(String serviceId, String action, HuaweiRequestObject huaweiRequestObject, JSONObject objManagerDataJson, String gatewayId, JSONObject requestJson) {
        //根据service和action，分发处理业务
        switch (WR20ServiceEnum.getByCode(serviceId)) {
            case FRAME_INFO_MANAGER:
                switch (action) {
                    case WR20ActionConstant.SYNC:
                        WR20CallbackFactory.getFactoryInstance().getFrameService(huaweiRequestObject.getSourceVersion()).syncFrame(objManagerDataJson.getJSONObject("objInfo"), gatewayId);
                        break;
                }
                break;
            case DEVICE_INFO_MANAGER:
                switch (action) {
                    case WR20ActionConstant.SYNC:
                        WR20CallbackFactory.getFactoryInstance().getDeviceService(huaweiRequestObject.getSourceVersion()).syncDevice(objManagerDataJson.getJSONObject("objInfo"), huaweiRequestObject.getWr20DeviceType(), gatewayId);
                        break;
                }
                break;
            case TENEMENT_MANAGER:
                switch (action) {
                    case WR20ActionConstant.SYNC:
                        WR20CallbackFactory.getFactoryInstance().getPersonService(huaweiRequestObject.getSourceVersion()).syncTenement(objManagerDataJson.getJSONObject("objInfo"), gatewayId);
                        break;
                    case WR20ActionConstant.ADD:
                        WR20CallbackFactory.getFactoryInstance().getPersonService(huaweiRequestObject.getSourceVersion()).addPerson(objManagerDataJson.getJSONObject("objInfo"), gatewayId, huaweiRequestObject.getUuid());
                        break;
                    case WR20ActionConstant.DELETE:
                        log.info("[华为中台] 获取到 [WR20] 迁出住户回调:{}", requestJson);
                        JSONObject objInfo = objManagerDataJson.getJSONObject("objInfo");
                        if (objInfo != null) {
                            log.info("[WR20] 迁出用户结果 ", objInfo.getString("message"));
                        }
                        break;
                }
                break;
            case WORKER_MANAGER:
                switch (action) {
                    case WR20ActionConstant.GET:
                        WR20CallbackFactory.getFactoryInstance().getPersonService(huaweiRequestObject.getSourceVersion()).syncStaff(objManagerDataJson.getJSONObject("objInfo"), gatewayId);
                        log.info("[华为中台] 获取到 [WR20] 查询员工回调:{}", requestJson);
                        break;
                    case WR20ActionConstant.ADD:
                        log.info("[华为中台] 获取到 [WR20] 添加员工回调:{}", requestJson);
                        WR20CallbackFactory.getFactoryInstance().getPersonService(huaweiRequestObject.getSourceVersion()).addStaff(objManagerDataJson.getJSONObject("objInfo"), gatewayId, huaweiRequestObject.getUuid());
                        break;
                    case WR20ActionConstant.DELETE:
                        log.info("[华为中台] 获取到 [WR20] 删除员工回调:{}", requestJson);
                        break;
                }
                break;
            case CERT_CARD:
                switch (action) {
                    case WR20ActionConstant.DELETE:
                        log.info("[华为中台] 获取到 [WR20] 删除卡片回调:{}", requestJson);
                        break;
                    case WR20ActionConstant.ADD:
                        log.info("[华为中台] 获取到 [WR20] 添加卡片回调:{}", requestJson);
                        String result = objManagerDataJson.getJSONObject("objInfo").getString("result");
                        if (StringUtils.isNotEmpty(result)) {
                            switch (result) {
                                case "0":
                                    log.info("[WR20] 添加卡片成功");
                                    break;
                                case "3":
                                    log.error("[WR20] 添加卡片失败，卡片已存在");
                                    break;
                            }
                        }
                        break;
                }
                break;
            case CERT_FACE:
                switch (action) {
                    case WR20ActionConstant.DELETE:
                        log.info("[华为中台] 获取到 [WR20] 删除人脸回调:{}", requestJson);
                        break;
                    case WR20ActionConstant.ADD:
                        log.info("[华为中台] 获取到 [WR20] 添加人脸回调:{}", requestJson);
                        WR20CallbackFactory.getFactoryInstance().getRightService(huaweiRequestObject.getSourceVersion()).addFace(objManagerDataJson.getJSONObject("objInfo"), gatewayId, huaweiRequestObject.getUuid());
                        break;
                }
                break;
            case VISITOR_MANAGER:
                switch (action) {
                    case WR20ActionConstant.ADD:
                        log.info("[华为中台] 获取到 [WR20] 添加访客回调:{}", requestJson);
                        /**
                         * {"onNotifyData":{"changeTime":"1611909327","devId":"5ebb5524f2d7e4035a3327a7_A97B934A-91CC-4152-BC76-B7ECBB0A9088","gatewayId":"5ebb5524f2d7e4035a3327a7_A97B934A-91CC-4152-BC76-B7ECBB0A9088","msgId":"ee8289937d984b1387e57cde25a52499","objManagerData":{"action":"ADD","objInfo":{"ThirdID":"","VIPK":20,"TeneID":1,"NotifyRepsonseID":"2ef6e13e-1547-47da-ae1f-c626d7aa59a6"},"objName":"VisitorInfo","serviceId":"VisitorManager"},"productId":"2"},"resource":"device.objmanager","event":"report","subscriptionId":"47"}
                         */
                        WR20CallbackFactory.getFactoryInstance().getPersonService(huaweiRequestObject.getSourceVersion()).addVisitor(objManagerDataJson.getJSONObject("objInfo"), gatewayId, huaweiRequestObject.getUuid());
                        break;
//                    case WR20ActionConstant.ADD:
//                        log.info("[华为中台] 获取到 [WR20] 添加人脸回调:{}", requestJson);
//                        WR20CallbackFactory.getFactoryInstance().getRightService(huaweiRequestObject.getSourceVersion()).addFace(objManagerDataJson.getJSONObject("objInfo"), gatewayId, huaweiRequestObject.getUuid());
//                        break;
                }
                break;
            default:
                log.error("[WR20] 未找到服务：{}", requestJson);
        }
    }

    /**
     * 华为中台 直连设备 请求回调处理函数
     */
    private void huaweiCallbackHandle(String serviceId, String thirdPartyCode, HuaweiRequestObject huaweiRequestObject, JSONObject objManagerDataJson, JSONObject requestJson) {
        List<ProjectRightDevice> rightDeviceList = null;
        JSONObject objInfo = null;
        String passNo = "";
        String keyid = "";
        //配置返回结果
        JSONObject resultObj = objManagerDataJson.getJSONObject("result");
        if (resultObj != null) {
            log.info("[华为中台] 获取到直连设备回调数据：{}", requestJson);
            ResponseOperateDTO responseOperateDTO = new ResponseOperateDTO();
            responseOperateDTO.setCompany(PlatformEnum.HUAWEI_MIDDLE.value);

            String result = resultObj.getString("result");// 返回结果


           /* if (result.equals(HuaweiConstant.SUCCESS))
                responseOperateDTO.setRespondStatus("1");
            else
                responseOperateDTO.setRespondStatus("0");*/

            //do business
            ProjectDeviceInfoProxyVo deviceInfo = projectDeviceInfoProxyService.getByThirdPartyCode(thirdPartyCode);
            if (deviceInfo == null) {
                log.error("[华为中台] 回调函数 未获取到设备{}", thirdPartyCode);
            } else {
                ProjectContextHolder.setProjectId(deviceInfo.getProjectId());
                responseOperateDTO.setDeviceInfo(deviceInfo);

                //人脸回应
                if (serviceId.equalsIgnoreCase(HuaweiServiceEnum.CERT_FACE.code)) {
                    objInfo = objManagerDataJson.getJSONObject("objInfo");
                    passNo = objInfo.getString("passNo");
                    keyid = objInfo.getString("keyid");
                    //获取到人脸对象
//                                rightDeviceList = projectRightDeviceMapper.selectList(new QueryWrapper<ProjectRightDevice>().lambda()
//                                        .eq(ProjectRightDevice::getDeviceId, deviceInfo.getDeviceId())
//                                        .eq(ProjectRightDevice::getCertmediaId, passNo));
                    rightDeviceList = projectRightDeviceMapper.listByDeviceIdAndCertmediaId(deviceInfo.getDeviceId(), keyid);

                    if (CollUtil.isNotEmpty(rightDeviceList)) {

                        HuaweiFaceResultCodeEnum certStatusByResult = HuaweiFaceResultCodeEnum.getCertStatusByResult(result);
                        log.info("[华为中台] 人脸介质keyid：{} 介质下发或删除结果：{} 设备端回调结果code：{}", keyid, certStatusByResult.desc, result);
                        if (PassRightCertDownloadStatusEnum.RE_DOWNLOAD.code.equals(certStatusByResult.cetDownloadStatus)) {
                            projectRightDeviceService.addCerts(rightDeviceList, true);
                            // 后面就没有必要执行了，因为重新下载（重试下载）是一个操作而不是状态
                            return;
                        } else {
                            responseOperateDTO.setRespondStatus(certStatusByResult.cetDownloadStatus);
                        }

                        responseOperateDTO.setAction(ResponseOperateConstant.ACTION_UPDATE_CERT_STATUS);
                        responseOperateDTO.setRightDevice(rightDeviceList.get(0));

                        this.sendMsg(TopicConstant.SDI_RESPONSE_OPERATE, responseOperateDTO);
                    } else {
                        log.error("[华为中台] 回调函数 在设备 {} {} 下未获取到通行凭证 {} ", deviceInfo.getDeviceName(), deviceInfo.getDeviceId(), keyid);
                    }

                } else {
                    //其他凭证回调
                }
            }
        } else {
            log.info("[华为中台] 获取到未定义的设备对象信息：{}", requestJson);
        }
    }

    /**
     * 设备操作指令的响应 数据清洗
     *
     * @param requestJson
     */
    @Override
    public void deviceCommandResponse(JSONObject requestJson) {
        sendMsg(TopicConstant.SDI_RESPONSE_OPERATE, requestJson);
    }


    /**
     * 设备事件上报 数据清洗
     * 调用 设备事件通知 主题
     *
     * @param requestJson
     */
    @Override
    public void deviceEventReport(JSONObject requestJson) {

        //init
        String openMode = "";//开门方式
        String userId = "";//住户第三方ID
        String userName = "";//住户第三方ID
        String passID = "";//凭证id


        //事件分类
        JSONObject onNotifyData = requestJson.getJSONObject("onNotifyData");
        String gatewayId = onNotifyData.getString("gatewayId");
        String deviceId = onNotifyData.getString("devId");

        JSONObject objManagerData = onNotifyData.getJSONObject("objManagerData");
        String eventCode = objManagerData.getString("eventCode");
        String eventName = objManagerData.getString("eventName");
        String eventSrc = objManagerData.getString("eventSrc");

//        Long eventTime = Long.valueOf(objManagerData.getString("eventTime"));//事件时间
//        LocalDateTime eventDate = DateUtil.toLocalDateTime(Instant.ofEpochSecond(eventTime));//事件时间
        String eventTime = objManagerData.getString("eventTime");
        LocalDateTime eventDate = null;
        if (StringUtil.isNumeric(eventTime)) {
//            Instant instant = Instant.ofEpochMilli(Long.valueOf(eventTime));
            Instant instant = Instant.ofEpochSecond(Long.valueOf(eventTime));
            eventDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        } else {
            eventDate = DateUtil.parseLocalDateTime(eventTime);
        }

        String eventType = objManagerData.getString("eventType");
        String objName = objManagerData.getString("objName");
        String serviceId = objManagerData.getString("serviceId");//事件类型


        //根据事件类型，自主拼接的事件内容，用于原生事件描述不完整的事件。
        String eventContent = "";
        HuaweiEventTypeEnum eventTypeEnum = HuaweiEventTypeEnum.getByCode(eventName);
        HuaweiEventEnum eventEnum = HuaweiEventEnum.getByCode(eventCode);
        eventContent = eventContent + eventTypeEnum.desc + " " + eventEnum.desc + " ";


        JSONObject dataJson = objManagerData.getJSONObject("data");
        /*********************************
         * 根据事件名称，分类事件（开门对象可以分类为，正常开门，异常开门等）
         ********************************/
        if (HuaweiEventTypeEnum.DjOpenDoorEvent.code.equals(eventName)) {//开门事件，使用通行对象


            OpendoorEventObj opendoorEventObj = dataJson.toJavaObject(OpendoorEventObj.class);

//            openMode = dataJson.getString("OpenMode");//开门方式
//            userId = dataJson.getString("userId");//住户第三方ID
//            eventContent = eventContent + "," + dataJson.getString("eventDesc");//事件描述
            openMode = opendoorEventObj.getOpenMode();
            passID = opendoorEventObj.getPassID();
            userName = opendoorEventObj.getUserName();
            userId = opendoorEventObj.getUserId();
            eventContent = eventContent + " " + opendoorEventObj.getEventDesc();//事件描述

            EventDeviceGatePassDTO eventDeviceGatePassDTO = new EventDeviceGatePassDTO();
            eventDeviceGatePassDTO.setCompany(PlatformEnum.HUAWEI_MIDDLE.name());
            eventDeviceGatePassDTO.setImgUrl(opendoorEventObj.getSnapPic());

            /**
             * 变更事件处理方法，从openMode改为根据json外层的eventCode识别
             * @auther:王伟
             * @since; 2021-04-27 17:08
             *
             * 2021-04-27 begin
             */
//
//            switch (openMode) {
//                case "1":
//                    eventDeviceGatePassDTO.setAction(EventDeviceGatePassConstant.ACTION_CAED);
//                    eventDeviceGatePassDTO.setCertMediaType(CertmediaTypeEnum.Card.code);
//                    //卡号如果少于8位，统一补0到8位
//                    passID = String.format("%08d", Integer.valueOf(passID));
//                    eventDeviceGatePassDTO.setCertMediaValue(passID);
//                    break;
//                case "2":
//                    eventDeviceGatePassDTO.setAction(EventDeviceGatePassConstant.ACTION_PASSWORD);
//                    eventDeviceGatePassDTO.setCertMediaType(CertmediaTypeEnum.Password.code);
//                    eventDeviceGatePassDTO.setCertMediaValue(passID);
//                    break;
//                case "3":
//                    eventDeviceGatePassDTO.setAction(EventDeviceGatePassConstant.ACTION_QR_CODE);
//                    break;
//                case "4":
//                    eventDeviceGatePassDTO.setAction(EventDeviceGatePassConstant.ACTION_FACE);
//                    eventDeviceGatePassDTO.setCertMediaType(CertmediaTypeEnum.Face.code);
////                    if (passID.length() <= 12) {//int型为faceCOde
//                    eventDeviceGatePassDTO.setCertMediaCode(passID);
////                    } else {//uuid为value,既faceId
////                        eventDeviceGatePassDTO.setCertMediaValue(passID);
////                    }
//                    break;
//                case "5":
//                    eventDeviceGatePassDTO.setAction(EventDeviceGatePassConstant.ACTION_FINGER);
//                case "21":
//                    eventDeviceGatePassDTO.setAction(EventDeviceGatePassConstant.ACTION_REMOTE);
//                    eventDeviceGatePassDTO = null;
//                    /**
//                     * 华为中台 远程开门事件屏蔽 仅记录同步事件
//                     */
//                    return;
////                    break;
//                default:
//                    eventDeviceGatePassDTO.setAction(EventDeviceGatePassConstant.OTHER);
//                    break;
//            }


            switch (eventEnum) {
                case OPEN_DOOR_CARD:
                    eventDeviceGatePassDTO.setAction(EventDeviceGatePassConstant.ACTION_CAED);
                    eventDeviceGatePassDTO.setCertMediaType(CertmediaTypeEnum.Card.code);
                    //卡号如果少于8位，统一补0到8位
                    passID = String.format("%08d", Integer.valueOf(passID));
                    eventDeviceGatePassDTO.setCertMediaValue(passID);
                    eventContent += "卡号：" + passID;
                    break;
                case OPEN_DOOR_FACE:
                    eventDeviceGatePassDTO.setAction(EventDeviceGatePassConstant.ACTION_FACE);
                    eventDeviceGatePassDTO.setCertMediaType(CertmediaTypeEnum.Face.code);
                    eventDeviceGatePassDTO.setCertMediaCode(passID);
                    break;
                case OPEN_DOOR_FINGER:
                    eventDeviceGatePassDTO.setAction(EventDeviceGatePassConstant.ACTION_FINGER);
                    break;

                case OPEN_DOOR_QR:
                    eventDeviceGatePassDTO.setAction(EventDeviceGatePassConstant.ACTION_QR_CODE_WITH_PERSON);
                    eventDeviceGatePassDTO.setCertMediaType(null);
                    eventDeviceGatePassDTO.setPersonType(PersonTypeEnum.PROPRIETOR.code);
//                    eventDeviceGatePassDTO.setPersonName(this.getHouseName(passID, " 住户"));
                    eventDeviceGatePassDTO.setPersonName(StringUtils.isEmpty(userName) ? this.getHouseName(passID, " 住户") : userName);
                    break;
                case OPEN_DOOR_QR_TEMP:
                    eventDeviceGatePassDTO.setAction(EventDeviceGatePassConstant.ACTION_QR_CODE_WITH_PERSON);
                    eventDeviceGatePassDTO.setCertMediaType(null);
                    eventDeviceGatePassDTO.setPersonType(PersonTypeEnum.VISITOR.code);
//                    eventDeviceGatePassDTO.setPersonName(this.getHouseName(passID, " 访客"));
                    eventDeviceGatePassDTO.setPersonName(StringUtils.isEmpty(userName) ? this.getHouseName(passID, " 访客") : userName);
                    break;
                default:
                    eventDeviceGatePassDTO.setAction(EventDeviceGatePassConstant.OTHER);
                    break;
            }

            /**
             * 2024-04-27 end
             */

//            eventDeviceGatePassDTO.setPersonCode(userId);

            eventDeviceGatePassDTO.setDesc(eventContent);
            eventDeviceGatePassDTO.setThirdPartyCode(deviceId);
            eventDeviceGatePassDTO.setEventTime(eventDate);
            eventDeviceGatePassDTO.setGatewayCode(gatewayId);

            sendMsg(TopicConstant.SDI_EVENT_DEVICE_GATE_PASS, eventDeviceGatePassDTO);


            //异常事件（告警）
        } else if (HuaweiEventTypeEnum.DjAbnormalEvent.code.equals(eventName)) {
            String desc = dataJson.getString("alarmDesc");
            if (StringUtils.isEmpty(desc)) {
                desc = dataJson.getString("EventDesc");
            }
            if (StringUtils.isNotEmpty(desc)) {
                eventContent = eventContent + "," + desc;//事件描述
            }

            EventDeviceGatePassDTO eventDeviceGatePassDTO = new EventDeviceGatePassDTO();
            eventDeviceGatePassDTO.setCompany(PlatformEnum.HUAWEI_MIDDLE.name());
            switch (openMode) {
                case "1":
                    eventDeviceGatePassDTO.setAction(EventDeviceGatePassConstant.ACTION_CAED);
                    break;
                case "4":
                    eventDeviceGatePassDTO.setAction(EventDeviceGatePassConstant.ACTION_FACE);
                    break;
            }

            eventDeviceGatePassDTO.setAction(EventDeviceGatePassConstant.ACTION_ERROR_PASS);
            eventDeviceGatePassDTO.setCertMediaType(null);
            eventDeviceGatePassDTO.setImgUrl(dataJson.getString("snapPic"));


//            EventWarningErrorDTO eventDeviceGatePassDTO = new EventWarningErrorDTO();
//            eventDeviceGatePassDTO.setAction(EventWarningErrorConstant.ACTION_ERROR);
            eventDeviceGatePassDTO.setCompany(PlatformEnum.HUAWEI_MIDDLE.name());
            eventDeviceGatePassDTO.setJsonObject(requestJson);

            eventDeviceGatePassDTO.setEventTime(eventDate);
            eventDeviceGatePassDTO.setDesc(eventContent);
            eventDeviceGatePassDTO.setThirdPartyCode(deviceId);
            eventDeviceGatePassDTO.setGatewayCode(gatewayId);


//            EventWarningErrorDTO eventWarningErrorDTO = new EventWarningErrorDTO();
//            eventWarningErrorDTO.setCompany(PlatformEnum.HUAWEI_MIDDLE.name());
//            eventWarningErrorDTO.setAction(EventWarningErrorConstant.ACTION_ERROR);
//
//            eventWarningErrorDTO.setDeviceCode(deviceId);
//            eventWarningErrorDTO.setDesc(eventContent);
//            eventWarningErrorDTO.setEventTime(eventDate);
//            eventWarningErrorDTO.setGatewayCode(gatewayId);
//            eventWarningErrorDTO.setImgUrl(dataJson.getString("snapPic"));

//            sendMsg(TopicConstant.SDI_EVENT_WARNING_ERROR, eventWarningErrorDTO);
            sendMsg(TopicConstant.SDI_EVENT_DEVICE_GATE_PASS, eventDeviceGatePassDTO);

            //报警事件
        } else if (HuaweiEventTypeEnum.DjAlarmEvent.code.equals(eventName)) {

            EventWarningErrorDTO eventWarningErrorDTO = new EventWarningErrorDTO();
            eventWarningErrorDTO.setCompany(PlatformEnum.HUAWEI_MIDDLE.name());
            eventWarningErrorDTO.setAction(EventWarningErrorConstant.ACTION_ERROR);

            eventWarningErrorDTO.setThirdPartyCode(deviceId);
            eventWarningErrorDTO.setDesc(eventContent);
            eventWarningErrorDTO.setEventTime(eventDate);
            eventWarningErrorDTO.setGatewayCode(gatewayId);
            eventWarningErrorDTO.setImgUrl(dataJson.getString("snapPic"));

            sendMsg(TopicConstant.SDI_EVENT_WARNING_ERROR, eventWarningErrorDTO);


        } else if (HuaweiEventTypeEnum.GwNormalEvent.code.equals(eventName)) {// WR20 通行凭证状态变更
            log.info("[WR20] - 获取到网关凭证状态变化通知：{}", objManagerData);
            WR20CallbackFactory.getFactoryInstance().getRightService(VersionEnum.V1.code).changeCertStateByWR20(objManagerData, gatewayId, eventCode);

        } else if (HuaweiEventTypeEnum.DjTalkRecordEvent.code.equals(eventName)) {
            // 通话记录事件
            log.info("[华为中台] - 获取到通话记录事件通知：{}", objManagerData);
            EventDeviceNoticeDTO eventDeviceNoticeDTO = new EventDeviceNoticeDTO();
            ProjectDeviceCallEvent deviceCallEvent = new ProjectDeviceCallEvent();
            JSONObject data = objManagerData.getJSONObject("data");
            RecordEventObj recordEventObj = JSONObject.parseObject(data.toJSONString(), RecordEventObj.class);


            //TODO:记录时间：2021-02-24  ；原因：这里得等后面对接
            /*List<ProjectDeviceInfo> callerDeviceList = projectDeviceInfoProxyService.list(new QueryWrapper<ProjectDeviceInfo>()
                    .lambda().eq(ProjectDeviceInfo::getDeviceCode, deviceId));
            if (CollUtil.isNotEmpty(callerDeviceList)) {
                ProjectDeviceInfo callerDevice = callerDeviceList.get(0);
                deviceCallEvent.setCallerName(StrUtil.isNotEmpty(callerDevice.getDeviceDesc()) ? callerDevice.getDeviceDesc() : callerDevice.getDeviceName());
                deviceCallEvent.setCaller(callerDevice.getDeviceId());
                deviceCallEvent.setProjectId(callerDevice.getProjectId());
            }*/

            //TODO:记录时间：2021-02-23；原因：呼叫方类型还不知道怎么判断，这里先默认设置为设备了
            deviceCallEvent.setCallerType("1");
            deviceCallEvent.setAnswererType(HuaweiCallTypeEnum.getTypeByCode(recordEventObj.getCallType()));

            //TODO:记录时间：2021-02-24  ；原因：这里得等后面对接
            /*List<ProjectDeviceInfo> answerList = projectDeviceInfoProxyService.list(new QueryWrapper<ProjectDeviceInfo>()
                    .lambda().eq(ProjectDeviceInfo::getDeviceCode, recordEventObj.getDeviceId()));
            if (CollUtil.isNotEmpty(answerList)) {
                ProjectDeviceInfo answerDevice = answerList.get(0);
                deviceCallEvent.setAnswererName(StrUtil.isNotEmpty(answerDevice.getDeviceDesc()) ? answerDevice.getDeviceDesc() : answerDevice.getDeviceName());
                deviceCallEvent.setAnswerer(answerDevice.getDeviceId());
            }*/

            //TODO:记录时间：2021-03-01；原因：暂时写死给设备测试用
            ProjectDeviceInfoProxyVo byThirdPartyCode = projectDeviceInfoProxyService.getByThirdPartyCode(deviceId);
            if (byThirdPartyCode != null) {
//                deviceCallEvent.setProjectId(byThirdPartyCode.getProjectId());
            }
            deviceCallEvent.setCallerName("陈光椿");
            deviceCallEvent.setCaller("32213123123213");
            deviceCallEvent.setAnswererName("王明星");
            deviceCallEvent.setAnswerer("1231424124214214");

            if (StrUtil.isNotEmpty(recordEventObj.getRecordTime())) {
                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                deviceCallEvent.setCallTime(LocalDateTime.parse(recordEventObj.getRecordTime(), dateFormat));
            }

            //TODO:记录时间：2021-02-23；原因：设备有三种呼叫结果 0：未接 1：已接 2：呼叫抓拍，系统只有前两种
            deviceCallEvent.setCallResponse(recordEventObj.getRecordMode());

            deviceCallEvent.setCallDuration(recordEventObj.getDuration());

            eventDeviceNoticeDTO.setDeviceCallEvent(deviceCallEvent);
            eventDeviceNoticeDTO.setAction(EventDeviceNoticeConstant.ACTION_DEVICE_CALL_EVENT);
            log.info("[华为中台] - 解析结果{}", eventDeviceNoticeDTO.toString());
            sendMsg(TopicConstant.SDI_EVENT_DEVICE_NOTICE, eventDeviceNoticeDTO);
//            eventDeviceNoticeConsumer.readActiveQueue(JSONObject.toJSONString(eventDeviceNoticeDTO));
        } else {
            log.info("[华为中台] - 获取到未知类型的事件通知：{}", objManagerData);
        }
    }

    @Autowired
    EventDeviceNoticeConsumer eventDeviceNoticeConsumer;

    /**
     * 发送消息到kafka
     *
     * @param topic   主题
     * @param message 内容体
     */
    private <T> void sendMsg(String topic, T message) {
        kafkaTemplate.send(topic, JSONObject.toJSONString(message));
    }

    /**
     * 校验是否为WR20设备
     *
     * @param gateWayId
     * @return
     */
    private boolean checkIsWR20(String gateWayId) {
        return wr20ConfigProperties.getProjectList().stream().filter(a -> a.get("wr20id") == gateWayId).count() >= 1;
    }

    /**
     * 通过房屋框架号，获取房屋名称
     *
     * @param keyValue
     * @return
     */
    private String getHouseName(String keyValue, String postfix) {
        String houseNo = "";
        String houseName = "";
        String groupName = "";
        if (StringUtils.isEmpty(keyValue)) {
            return "";
        }

        //如果存在多个房间号，获取第一个
        if (keyValue.indexOf(",") >= 0) {
            String[] houseNoArray = keyValue.split(",");
            for (String no : houseNoArray) {
                if (no.length() >= 5) {
                    houseNo = no;
                    break;
                }
            }
        } else {
            houseNo = keyValue;
        }

        //调用业务服务，获取房间名称
        ProjectFrameInfo frameInfo = projectFrameInfoService.getByFrameNo(houseNo);
        if (frameInfo != null) {
            houseName = projectFrameInfoService.getFrameNameByEntityId(frameInfo.getEntityId(), "-", null, FrameLevelEnum.BUILDING.code);
            groupName = projectFrameInfoService.getFrameNameByEntityId(frameInfo.getEntityId(), "-", FrameLevelEnum.GROUP4.code, null);
        }

        if (StringUtils.equals(houseNo, "00000000")) {
            houseName = "公共区域";
        }

        /**
         * DEMO:组团5-组团4-楼栋单元房屋 + 后缀名词
         */
        houseName = StringUtils.isEmpty(houseName) ? "" : houseName;
        groupName = StringUtils.isEmpty(groupName) ? "" : groupName + "-";

        String resultStr = groupName + houseName;

        resultStr = StringUtils.isEmpty(resultStr) ? "" : resultStr + postfix;

        return resultStr;
    }


    /**
     * TODO 等待3.0设备 V2版本二维码 校验返回的事件数据结构 @since 2021-05-06 王伟
     * 获取版本2 二维码事件表述信息
     *
     * @param eventDeviceGatePassDTO
     * @param userNo                 当前访问返回的userNo(序列编码)
     */
    private void getQRV2PassEventDesc(EventDeviceGatePassDTO eventDeviceGatePassDTO, String userNo) {

        Object qrDtoObject = RedisUtil.get("QR_" + userNo);
        if (qrDtoObject == null) {
            //未获取到数据（超时或其他异常原因，如redis重启，清空）

        } else {
            ProjectDevicePassQRDTO qrDto = (ProjectDevicePassQRDTO) qrDtoObject;
            eventDeviceGatePassDTO.setPersonName(qrDto.getPersonName());
            eventDeviceGatePassDTO.setPersonCode(qrDto.getPersonId());
            eventDeviceGatePassDTO.setPersonType(qrDto.getPersonType().code);
        }

    }
}
