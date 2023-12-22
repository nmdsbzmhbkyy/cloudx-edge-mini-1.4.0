package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.constant.DeviceTypeConstants;
import com.aurine.cloudx.common.core.constant.enums.DeviceTypeEnum;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.dto.DeviceRegDto;
import com.aurine.cloudx.estate.dto.DeviceStatusDto;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.DeviceService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto.AurineEdgeDeviceInfoDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.config.HuaweiConfigDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.config.HuaweiSubscribeDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.HuaweiProduct;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.constant.*;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.dto.HuaweiRespondDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.remote.factory.HuaweiRemoteDeviceOperateServiceFactory;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.remote.factory.HuaweiRemoteDeviceServiceFactory;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.remote.factory.HuaweiRemoteSubscriptionServiceFactory;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.util.HuaweiDeviceTypeUtil;
import com.aurine.cloudx.estate.util.ObjectMapperUtil;
import com.aurine.cloudx.estate.vo.MediaAdInfoVo;
import com.aurine.cloudx.estate.vo.ProjectDeviceGatherAlarmRuleVo;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoProxyVo;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 华为中台 V1 版本 对接业务实现
 *
 * @ClassName:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-07-31 13:44
 * @Copyright:
 */
@Service
@Slf4j
public class DeviceServiceImplByHuaweiV1 implements DeviceService {

    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;
    @Resource
    private SysDeviceTypeThirdPartyConfigService sysDeviceTypeThirdPartyConfigService;
    @Resource
    private SysDeviceProductMapService sysDeviceProductMapService;


    @Resource
    private ProjectBuildingInfoService projectBuildingInfoService;

    @Resource
    private ProjectDeviceInfoProxyService projectDeviceInfoProxyService;

    @Resource
    private SysDeviceTypeModelService sysDeviceTypeModelService;

    @Resource
    private ProjectInfoService projectInfoService;

    @Resource
    private SysDeviceTypeModelTypeConfigService sysDeviceTypeModelTypeConfigService;

    private static ObjectMapper objectMapper = ObjectMapperUtil.instance();

    static {
        // 这里设置objectMapper 如果在反序列化的时候json中没有某个属性，则设置默认值避免发生空指针异常
        InjectableValues.Std std = new InjectableValues.Std();
        // capabilities是注解在对象属性上的 @JacksonInject("capabilities")
        std.addValue("capabilities", objectMapper.createArrayNode());
        objectMapper.setInjectableValues(std);
    }

    /**
     * 订阅消息
     *
     * @return
     */
    @Override
    public boolean subscribe(String deviceType, int projectId, int tenantId) {
        HuaweiConfigDTO config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceType(deviceType, projectId, tenantId, HuaweiConfigDTO.class);
        HuaweiRespondDTO respondDTO = null;

        for (HuaweiSubscribeDTO subscribeDTO : config.getSubscribe()) {
            if (subscribeDTO.getEnable()) {
                respondDTO = HuaweiRemoteSubscriptionServiceFactory.getInstance(getVer()).addSubscriptions(config, subscribeDTO.getUrl(), "http", subscribeDTO.getResource(), subscribeDTO.getEvent());

                if (respondDTO.getErrorEnum() == HuaweiErrorEnum.SUCCESS) {
                    //成功业务
                }
            }

        }
        return true;
    }


    /**
     * 同步设备或产品
     *
     * @param deviceType -1时 为同步产品（未定义）
     * @param projectId
     * @param tenantId
     * @return
     */
    @Override
    public boolean syncDecvice(String deviceType, int projectId, int tenantId) {
        return false;
    }

    @Override
    public boolean dealAlarm(String eventId) {
        return false;
    }

    @Override
    public boolean sendMediaAd(String deviceId, MediaAdInfoVo media) {
        return false;
    }

    /**
     * 新增设备
     * <p>
     * respond
     * {
     * "errorCode": 0,
     * "body": [
     * {
     * "devId": "5ed9dc514b495808c82eca87_PEOQL20M08029761S9DJ",
     * "devSN": "PEOQL20M08029761S9DJ",
     * "devStatus": 1,
     * "productId": "5",
     * "modelId": "020303",
     * "lastOnLineDate": 1599796078000,
     * "name": "梯口测试机04",
     * "deviceNo": "PEOQL20M08029761S9DJ",
     * "manufacturer": "米立",
     * "createDate": 1599009196000
     * }
     * ],
     * "errorMsg": "操作成功"
     * }
     *
     * @param deviceInfo
     * @return
     */
    @Override
    public String addDevice(ProjectDeviceInfoProxyVo deviceInfo) {
        String thirdDeviceId = "", //设备第三方编号
                devStatus = "",//设备状态 0 未激活 1在线 2离线
                productCode = "", //产品编码
                productId = "", //产品id
                modelId = "",//模型id
                devResType = "",//设备资源类型
                devMac = "",//设备Mac地址
                lastOnLineDate = ""//最后的上线时间
                        ;

        if (deviceInfo != null && StringUtil.isNotEmpty(deviceInfo.getSn())) {
            //获取对接配置
            /**
             * WR20网关适配
             */
            HuaweiConfigDTO config = null;
            if (deviceInfo.getDeviceType().equals(DeviceTypeEnum.WR20_DEVICE.getCode())) {
                config = sysDeviceTypeThirdPartyConfigService.getConfigByProjectId(deviceInfo.getProjectId(), deviceInfo.getTenantId(), HuaweiConfigDTO.class);
            } else {
                config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceType(deviceInfo.getDeviceType(), deviceInfo.getProjectId(), deviceInfo.getTenantId(), HuaweiConfigDTO.class);
            }
            //调用对接业务
            HuaweiRespondDTO respondDTO = HuaweiRemoteDeviceServiceFactory.getInstance(getVer()).addDevice(config, deviceInfo.getSn());

            if (respondDTO.getErrorEnum() == HuaweiErrorEnum.SUCCESS) {
                if (CollUtil.isEmpty(respondDTO.getBodyArray())) {
                    //对于单次添加，只返回成功的数据
                    log.error("[华为中台] 未找到SN为{} 的设备", deviceInfo.getSn());
                    throw new RuntimeException("设备不存在");
                } else {

                    for (JSONObject respondObj : respondDTO.getBodyArray().toJavaList(JSONObject.class)) {
                        /*{
                            "errorCode": 0,
                                "errorMsg": "操作成功",
                                "body": [
                                    {
                                        "devId": "60a4a76f9f8b7602c0d03a2e_9G2H0000112T",
                                        "name": "9G2H0000112T",
                                        "productId": "13",
                                        "modelId": "3T11T3C01",
                                        "manufacturer": "米立",
                                        "deviceNo": "9G2H0000112T",
                                        "devSN": "9G2H0000112T",
                                        "mac": "00-55-52-46-a3-a7",
                                        "devStatus": 1,
                                        "createDate": 1621403729000,
                                        "lastOnLineDate": 1621403758000
                                    }
                                ]
                        }*/

                        thirdDeviceId = respondObj.getString("devId");
                        devStatus = respondObj.getString("devStatus");
                        // 不使用他们的productId因为这个是他们的自增序列，不同的中台会不一样有问题，这里使用productKey
//                        productId = respondObj.getString("productId");
                        productCode = respondObj.getString("productKey");
                        productId = respondObj.getString("productKey");
                        modelId = respondObj.getString("modelId");
                        lastOnLineDate = respondObj.getString("lastOnLineDate");
                        lastOnLineDate = respondObj.getString("lastOnLineDate");
                        devMac = respondObj.getString("mac");
                        devResType = respondObj.getString("devResType");

                        SysDeviceProductMap productMap = sysDeviceProductMapService.getOne(new LambdaQueryWrapper<SysDeviceProductMap>().eq(SysDeviceProductMap::getProductCode, productCode));
                        if (productMap == null) {
                            throw new RuntimeException("未找到该设备对应的产品，需要重新同步产品");
                        }
                        deviceInfo.setProductId(productMap.getProductId());
                        deviceInfo.setThirdpartyCode(thirdDeviceId);
                        deviceInfo.setMac(devMac);
                        deviceInfo.setResType(devResType);
                        deviceInfo.setStatus(HuaweiOnlineStatusEnum.getByCode(devStatus).cloudCode);

                        if (deviceInfo.getDeviceType().equals(DeviceTypeEnum.WR20_DEVICE)) {
                            //WR20要出发的特殊流程
                        } else {
                            //直连设备添加完成后出发的流程
                            //查询设备的ip，Mac地址等
//                            HuaweiRemoteDeviceOperateServiceFactory.getInstance(getVer()).objectManage(config, thirdDeviceId, HuaweiServiceEnum.DEVICE_PARAM.code, HuaweiActionConstant.GET, "NetworkObj", new JSONArray(), null);
//                            //查询设备的框架号
//                            HuaweiRemoteDeviceOperateServiceFactory.getInstance(getVer()).objectManage(config, thirdDeviceId, HuaweiServiceEnum.DEVICE_PARAM.code, HuaweiActionConstant.GET, "DeviceNoObj", new JSONArray(), null);
                        }
                        List<String> thirdCodeList = new ArrayList<>();
                        thirdCodeList.add(thirdDeviceId);
                        if (DeviceTypeConstants.GATE_DEVICE.equals(deviceInfo.getDeviceType()) ||
                                DeviceTypeConstants.LADDER_WAY_DEVICE.equals(deviceInfo.getDeviceType())) {
                            this.addDeviceToDeviceGroup(thirdCodeList);
                        }
                        return thirdDeviceId;
                    }
                }
            } else {
                //异常处理
                return null;
            }


        } else {
            throw new RuntimeException("设备 :" + deviceInfo.getDeviceName() + " 缺失SN参数");
        }
        return null;
    }

    @Override
    public String addDevice(ProjectDeviceInfoProxyVo deviceInfo, String productCode) {
        String thirdDeviceId = "", //设备第三方编号
                devStatus = "",//设备状态 0 未激活 1在线 2离线
                productId = "", //产品id
                modelId = "",//模型id
                devResType = "",//设备资源类型
                devMac = "",//设备Mac地址
                lastOnLineDate = ""//最后的上线时间
                        ;

        if (deviceInfo != null && StringUtil.isNotEmpty(deviceInfo.getSn())) {
            //获取对接配置
            /**
             * WR20网关适配
             */
            HuaweiConfigDTO config = null;
            if (deviceInfo.getDeviceType().equals(DeviceTypeEnum.WR20_DEVICE.getCode())) {
                config = sysDeviceTypeThirdPartyConfigService.getConfigByProjectId(deviceInfo.getProjectId(), deviceInfo.getTenantId(), HuaweiConfigDTO.class);
            } else {
                config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceType(deviceInfo.getDeviceType(), deviceInfo.getProjectId(), deviceInfo.getTenantId(), HuaweiConfigDTO.class);
            }
            //调用对接业务
            HuaweiRespondDTO respondDTO = HuaweiRemoteDeviceServiceFactory.getInstance(getVer()).addDevice(config, deviceInfo.getSn(), productCode);

            if (respondDTO.getErrorEnum() == HuaweiErrorEnum.SUCCESS) {
                if (!CollUtil.isEmpty(respondDTO.getBodyArray())) {

                    for (JSONObject respondObj : respondDTO.getBodyArray().toJavaList(JSONObject.class)) {
                        /*{
                            "errorCode": 0,
                                "errorMsg": "操作成功",
                                "body": [
                                    {
                                        "devId": "60a4a76f9f8b7602c0d03a2e_9G2H0000112T",
                                        "name": "9G2H0000112T",
                                        "productId": "13",
                                        "modelId": "3T11T3C01",
                                        "manufacturer": "米立",
                                        "deviceNo": "9G2H0000112T",
                                        "devSN": "9G2H0000112T",
                                        "mac": "00-55-52-46-a3-a7",
                                        "devStatus": 1,
                                        "createDate": 1621403729000,
                                        "lastOnLineDate": 1621403758000
                                    }
                                ]
                        }*/

                        thirdDeviceId = respondObj.getString("devId");
                        devStatus = respondObj.getString("devStatus");
                        productCode = respondObj.getString("productKey");
                        modelId = respondObj.getString("modelId");
                        lastOnLineDate = respondObj.getString("lastOnLineDate");
                        lastOnLineDate = respondObj.getString("lastOnLineDate");
                        devMac = respondObj.getString("mac");
                        devResType = respondObj.getString("devResType");

                        deviceInfo.setThirdpartyCode(thirdDeviceId);
                        deviceInfo.setMac(devMac);
                        deviceInfo.setResType(devResType);
                        deviceInfo.setStatus(HuaweiOnlineStatusEnum.getByCode(devStatus).cloudCode);

                        if (deviceInfo.getDeviceType().equals(DeviceTypeEnum.WR20_DEVICE)) {
                            //WR20要出发的特殊流程
                        } else {
                            //直连设备添加完成后出发的流程
                            //查询设备的ip，Mac地址等
//                            HuaweiRemoteDeviceOperateServiceFactory.getInstance(getVer()).objectManage(config, thirdDeviceId, HuaweiServiceEnum.DEVICE_PARAM.code, HuaweiActionConstant.GET, "NetworkObj", new JSONArray(), null);
//                            //查询设备的框架号
//                            HuaweiRemoteDeviceOperateServiceFactory.getInstance(getVer()).objectManage(config, thirdDeviceId, HuaweiServiceEnum.DEVICE_PARAM.code, HuaweiActionConstant.GET, "DeviceNoObj", new JSONArray(), null);
                        }
                        List<String> thirdCodeList = new ArrayList<>();
                        thirdCodeList.add(thirdDeviceId);
                        if (DeviceTypeConstants.GATE_DEVICE.equals(deviceInfo.getDeviceType()) ||
                                DeviceTypeConstants.LADDER_WAY_DEVICE.equals(deviceInfo.getDeviceType())) {
                            this.addDeviceToDeviceGroup(thirdCodeList);
                        }
                        return thirdDeviceId;
                    }
                }

                SysDeviceProductMap productMap = sysDeviceProductMapService.getOne(new LambdaQueryWrapper<SysDeviceProductMap>().eq(SysDeviceProductMap::getProductCode, productCode));
                if (productMap == null) {
                    throw new RuntimeException("未找到该设备对应的产品，需要重新同步产品");
                }
                deviceInfo.setProductId(productMap.getProductId());
            } else {
                //异常处理
                return null;
            }


        } else {
            throw new RuntimeException("设备 :" + deviceInfo.getDeviceName() + " 缺失SN参数");
        }
        return null;
    }

    @Override
    public String[] addDeviceBatch(List<ProjectDeviceInfoProxyVo> deviceList, String productKey) {
        String[] resultArray = new String[0];
        if (CollUtil.isNotEmpty(deviceList)) {

            resultArray = new String[deviceList.size()];

            ProjectDeviceInfoProxyVo deviceInfo = deviceList.get(0);
            HuaweiConfigDTO config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceType(deviceInfo.getDeviceType(), deviceInfo.getProjectId(), deviceInfo.getTenantId(), HuaweiConfigDTO.class);

            //根据设备类型，获取对应的
            HuaweiRespondDTO respondDTO = HuaweiRemoteDeviceServiceFactory.getInstance(this.getVer())
                    .addDeviceBatch(config, deviceList.stream().map(e -> e.getSn()).collect(Collectors.toList()).toArray(new String[deviceList.size()]), productKey);
            /*
             *[
             *    {
             *        "devId": "5fe40e5e5c886202ce5ff5a8_3G2H0000032T",
             *        "name": "3G2H0000032T",
             *        "productId": "7",
             *        "modelId": "3T11K4C01",
             *        "manufacturer": "米立",
             *        "deviceNo": "3G2H0000032T",
             *        "devSN": "3G2H0000032T",
             *        "mac": "66-1b-2e-32-e8-45",
             *        "devStatus": 1,
             *        "createDate": 1618902319000,
             *        "lastOnLineDate": 1622180358000
             *    }
             *]
             *
             *
             * */
            try {
                ArrayNode bodyArrayNode = objectMapper.readValue(respondDTO.getBodyArray().toJSONString(), ArrayNode.class);
                if (bodyArrayNode.isContainerNode()) {
                    List<String> devIdList = bodyArrayNode.findValuesAsText("devId", new ArrayList<>());
                    this.addDeviceToDeviceGroup(devIdList);
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            //TODO 王伟 需要完成普联设备添加的respond识别
        }

        return resultArray;
    }

    /**
     * 批量新增设备
     *
     * @param deviceList
     * @return
     */
    @Override
    public String[] addDeviceBatch(List<ProjectDeviceInfoProxyVo> deviceList) {
        String[] resultArray = new String[0];
        if (CollUtil.isNotEmpty(deviceList)) {

            resultArray = new String[deviceList.size()];

            ProjectDeviceInfoProxyVo deviceInfo = deviceList.get(0);
            HuaweiConfigDTO config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceType(deviceInfo.getDeviceType(), deviceInfo.getProjectId(), deviceInfo.getTenantId(), HuaweiConfigDTO.class);

            //根据设备类型，获取对应的
            HuaweiRespondDTO respondDTO = HuaweiRemoteDeviceServiceFactory.getInstance(this.getVer()).addDeviceBatch(config, deviceList.stream().map(e -> e.getSn()).collect(Collectors.toList()).toArray(new String[deviceList.size()]));
            /*
             *[
             *    {
             *        "devId": "5fe40e5e5c886202ce5ff5a8_3G2H0000032T",
             *        "name": "3G2H0000032T",
             *        "productId": "7",
             *        "modelId": "3T11K4C01",
             *        "manufacturer": "米立",
             *        "deviceNo": "3G2H0000032T",
             *        "devSN": "3G2H0000032T",
             *        "mac": "66-1b-2e-32-e8-45",
             *        "devStatus": 1,
             *        "createDate": 1618902319000,
             *        "lastOnLineDate": 1622180358000
             *    }
             *]
             *
             *
             * */
            try {
                ArrayNode bodyArrayNode = objectMapper.readValue(respondDTO.getBodyArray().toJSONString(), ArrayNode.class);
                if (bodyArrayNode.isContainerNode()) {
                    List<String> devIdList = bodyArrayNode.findValuesAsText("devId", new ArrayList<>());
                    this.addDeviceToDeviceGroup(devIdList);
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            //TODO 王伟 需要完成普联设备添加的respond识别
        }

        return resultArray;
    }

    /**
     * 修改设备
     *
     * @param device
     * @return
     */
    @Override
    public boolean updateDevice(ProjectDeviceInfoProxyVo device) {
        //解析需要更新的内容
        //只能修改IP
        if (StringUtils.isEmpty(device.getThirdpartyCode())) {
            log.error("[华为中台] 设备{} 无第三方编号，跳过更新流程", device);
            return false;
        }

        HuaweiConfigDTO config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceId(device.getDeviceId(), HuaweiConfigDTO.class);

        JSONArray propertiesArrayJson = new JSONArray();
        JSONObject propertiesJson = new JSONObject();

        JSONObject valueJson = new JSONObject();
        valueJson.put("ipAddr", device.getIpv4());


        propertiesJson.put("serviceId", HuaweiServiceEnum.DEVICE_PARAM.code);
        propertiesJson.put("propertieTag", "netparam");
        propertiesJson.put("value", valueJson);
        propertiesArrayJson.add(propertiesJson);
        //TODO 和良均确认输出结构和内容。

        HuaweiRemoteDeviceOperateServiceFactory.getInstance(getVer()).propertiesDown(config, device.getThirdpartyCode(), "", propertiesArrayJson, null);
        return true;
    }

    @Override
    public boolean getDeviceParam(String serviceId, String thirdpartyCode, String deviceId) {
        HuaweiConfigDTO config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceId(deviceId, HuaweiConfigDTO.class);
        log.info("[华为中台] 设备参数获取-本次获取参数请求{},{},{}", serviceId, thirdpartyCode, deviceId);
        HuaweiRemoteDeviceOperateServiceFactory.getInstance(getVer()).objectManage(config, thirdpartyCode,
                HuaweiServiceEnum.DEVICE_PARAM.code, HuaweiActionConstant.GET, serviceId, new JSONArray(), null);
        return true;
    }

    @Override
    public boolean getDeviceInfo(String deviceId, String thirdpartyCode) {
        HuaweiConfigDTO config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceId(deviceId, HuaweiConfigDTO.class);
        log.info("[华为中台] 设备参数获取-本次获取设备信息请求{},{}", thirdpartyCode, deviceId);
        HuaweiRemoteDeviceOperateServiceFactory.getInstance(getVer()).objectManage(config, thirdpartyCode,
                HuaweiServiceEnum.DEVICE_INFO.code, HuaweiActionConstant.GET, "", new JSONArray(), null);
        return true;
    }


    /**
     * 删除设备
     *
     * @param deviceId
     * @return
     */
    @Override
    public boolean delDevice(String deviceId) {
        ProjectDeviceInfoVo projectDeviceInfoVo = projectDeviceInfoService.getProjectDeviceInfoById(deviceId);
        ProjectDeviceInfoProxyVo deviceInfo = projectDeviceInfoProxyService.getVoById(deviceId);

        //设备不存在第三方id，直接删除设备
        if (StringUtils.isEmpty(deviceInfo.getThirdpartyCode())) {
            return true;
        }

        if (deviceInfo != null && StringUtil.isNotEmpty(deviceInfo.getSn())) {
            //获取对接配置
            HuaweiConfigDTO config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceId(deviceId, HuaweiConfigDTO.class);
            //调用对接业务
            HuaweiRespondDTO respondDTO = HuaweiRemoteDeviceServiceFactory.getInstance(getVer()).delDevice(config, deviceInfo.getThirdpartyCode());

            if (respondDTO.getErrorEnum() == HuaweiErrorEnum.SUCCESS || respondDTO.getErrorEnum() == HuaweiErrorEnum.NO_DEVICE) {
                log.info("[华为中台] 删除设备成功 {} {} ", deviceInfo.getDeviceId(), deviceInfo.getDeviceName());
                // 添加设备到设备组
                if (DeviceTypeConstants.LADDER_WAY_DEVICE.equals(deviceInfo.getDeviceType())
                        || DeviceTypeConstants.GATE_DEVICE.equals(deviceInfo.getDeviceType())) {
                    List<String> devIdList = new ArrayList<>();
                    devIdList.add(deviceInfo.getThirdpartyCode());
                    this.removeDeviceFromDeviceGroup(devIdList);
                }
                return true;
            } else {
                log.info("[华为中台] 删除设备失败 {} {} ", deviceInfo.getDeviceId(), deviceInfo.getDeviceName());
                return false;
            }

        } else {
            return true;
        }
    }

    /**
     * 重启
     *
     * @param deviceId 设备id
     * @return
     */
    @Override
    public boolean reboot(String deviceId) {
        //华为中台的开门业务处理
        ProjectDeviceInfo deviceInfo = projectDeviceInfoService.getById(deviceId);
        HuaweiConfigDTO config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceId(deviceId, HuaweiConfigDTO.class);

        if (deviceInfo != null && StringUtil.isNotEmpty(deviceInfo.getThirdpartyCode())) {
            //调用对接业务
            HuaweiRespondDTO huaweiRespondDTO = HuaweiRemoteDeviceOperateServiceFactory.
                    getInstance(getVer()).commandsDown(config, deviceInfo.getThirdpartyCode(),
                    HuaweiServiceEnum.DEVICE_CONTROL.code, HuaweiCommandConstant.REBOOT, null, null);
            return huaweiRespondDTO.getErrorEnum() == HuaweiErrorEnum.SUCCESS;
        } else {
            throw new RuntimeException("设备 " + deviceId + " 缺失对接参数");
        }
    }

    @Override
    public boolean callElevator(String deviceId, String roomCode, String floor, String liftDirect) {
        return false;
    }

    @Override
    public boolean reset(String deviceId) {
        ProjectDeviceInfo deviceInfo = projectDeviceInfoService.getById(deviceId);
        HuaweiConfigDTO config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceId(deviceId, HuaweiConfigDTO.class);
        if (deviceInfo != null && StringUtil.isNotEmpty(deviceInfo.getThirdpartyCode())) {
            HuaweiRespondDTO respondDTO = HuaweiRemoteDeviceOperateServiceFactory.getInstance(getVer()).commandsDown(config, deviceInfo.getThirdpartyCode(),
                    HuaweiServiceEnum.DEVICE_CONTROL.code, HuaweiCommandConstant.RESET, null, null);
            return respondDTO.getErrorEnum() == HuaweiErrorEnum.SUCCESS;
        }
        return false;
    }

    @Override
    public boolean setPwd(String deviceId, String password) {
        String pwd = password.trim();
        if (pwd.length() != 8) {
            return false;
        }
        ProjectDeviceInfo deviceInfo = projectDeviceInfoService.getById(deviceId);
        HuaweiConfigDTO config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceId(deviceId, HuaweiConfigDTO.class);
        if (deviceInfo != null && StringUtil.isNotEmpty(deviceInfo.getThirdpartyCode())) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("password", password);
            HuaweiRespondDTO respondDTO = HuaweiRemoteDeviceOperateServiceFactory.getInstance(getVer()).commandsDown(config, deviceInfo.getThirdpartyCode(),
                    HuaweiServiceEnum.DEVICE_CONTROL.code, HuaweiCommandConstant.SET_S_PWD, jsonObject, null);
            return respondDTO.getErrorEnum() == HuaweiErrorEnum.SUCCESS;
        }
        return false;
    }

    /**
     * 同步产品(同步)
     *
     * @return
     */
    @Override
    public boolean syncProduces(int projectId, int tenantId) {
        log.info("[华为中台] 开始同步产品");
        //init
        int pageNo = 1;//当前页，根据页数自增
        Integer total = 0;
        HuaweiRespondDTO respondDTO = null;

        List<SysDeviceProductMap> deviceProductMapList = new ArrayList<>();
        JSONArray jsonArray = null;

        String modelId;
        String produceName;

        HuaweiConfigDTO huaweiConfig = null;

        //获取配置
        huaweiConfig = sysDeviceTypeThirdPartyConfigService.getConfigByProjectId(projectId, tenantId, HuaweiConfigDTO.class);
        if (huaweiConfig == null) {
            throw new RuntimeException("同步产品失败，未获取到当前项目配置信息");
        }

        JSONObject resultJson = null;
        do {
            respondDTO = HuaweiRemoteDeviceServiceFactory.getInstance(VersionEnum.V1).queryProducts(huaweiConfig, pageNo, huaweiConfig.getTransferBufferSize());
            if (respondDTO.getErrorEnum() == HuaweiErrorEnum.SUCCESS) {
                resultJson = respondDTO.getBodyObj();

                total = resultJson.getIntValue("total");

                if (total == null) {
                    log.error("[华为中台] 同步产品数据第{}页失败，数据{}", pageNo, resultJson);
                    throw new RuntimeException("同步产品数据第" + pageNo + "页失败");
                }
                jsonArray = resultJson.getJSONArray("list");

                //解析产品类型并生成产品列表（按model id 以及 产品名称识别）
                // 同步产品
                List<SysDeviceProductMap> productMapList = createDeviceProductBean(jsonArray, projectId);
                sysDeviceProductMapService.syncProductList(productMapList);
                this.saveProductDeviceTypeRel(jsonArray.toJSONString(), projectId);
            }

            pageNo++;
        } while (total - (pageNo * huaweiConfig.getTransferBufferSize()) >= 0 || (pageNo * huaweiConfig.getTransferBufferSize()) - total < huaweiConfig.getTransferBufferSize()); //剩余条数小于0时，代表不存在更多分页，结束遍历


        return true;
    }


    private void saveProductDeviceTypeRel(String json, Integer projectId) {
        SysThirdPartyInterfaceConfig configByProjectId = sysDeviceTypeThirdPartyConfigService.getConfigByProjectId(projectId);
        this.saveProductDeviceTypeRel(json, configByProjectId);
    }

    private void saveProductDeviceTypeRel(String json, SysThirdPartyInterfaceConfig config) {
        List<SysDeviceTypeModel> sysDeviceTypeModelList = new ArrayList<>();
        try {
            ArrayNode productArr = objectMapper.readValue(json, ArrayNode.class);
            List<SysDeviceProductMap> productMapList = sysDeviceProductMapService.list(new LambdaQueryWrapper<SysDeviceProductMap>().eq(SysDeviceProductMap::getThirdPartyNo, config.getThirdPartyNo()));
            Map<String, String> productMap = productMapList.stream().collect(Collectors.toMap(SysDeviceProductMap::getProductCode, SysDeviceProductMap::getProductId, (s, s2) -> s2));
            List<SysDeviceTypeModel> deviceTypeModelList = sysDeviceTypeModelService.list(new LambdaQueryWrapper<SysDeviceTypeModel>().select(SysDeviceTypeModel::getProductId, SysDeviceTypeModel::getDeviceTypeId));
            if (CollUtil.isEmpty(deviceTypeModelList)) {
                deviceTypeModelList = new ArrayList<>();
            }
            Set<String> existDeviceTypeModelSet = deviceTypeModelList.stream().map(sysDeviceTypeModel -> sysDeviceTypeModel.getProductId() + sysDeviceTypeModel.getDeviceTypeId()).collect(Collectors.toSet());
            productArr.forEach(node -> {
                JsonNode modelType = node.findPath("modelType");

                JsonNode productCode = node.findPath("productKey");
                List<String> deviceTypeList = sysDeviceTypeModelTypeConfigService.listDeviceType(modelType.asText(), PlatformEnum.getByValue(config.getName()));

//                Set<String> deviceTypeListByModelIdSet = ProductTypeEnum.getDeviceTypeListByModelType(modelType.asText());

                if (CollUtil.isEmpty(deviceTypeList)) {
                    return;
                }
                deviceTypeList.forEach(item -> {
                    if (!existDeviceTypeModelSet.contains(productMap.get(productCode.asText()) + item)) {
                        sysDeviceTypeModelList.add(new SysDeviceTypeModel(item, productMap.get(productCode.asText()), 1, 1));
                        existDeviceTypeModelSet.add(productMap.get(productCode.asText()) + item);
                    }
                });
            });

            sysDeviceTypeModelService.saveBatch(sysDeviceTypeModelList);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成系统内部的设备产品映射表对象列表
     *
     * @param jsonArray
     * @return
     */
    private List<SysDeviceProductMap> createDeviceProductBean(JSONArray jsonArray, int projectId) {
        SysThirdPartyInterfaceConfig configByProjectId = sysDeviceTypeThirdPartyConfigService.getConfigByProjectId(projectId);
        SysDeviceProductMap deviceProductMapPo = new SysDeviceProductMap();
        JSONObject productJson = null;
        List<SysDeviceProductMap> deviceProductMapList = new ArrayList<>();
        String deviceTypeId = null;

        if (jsonArray == null) {
            return deviceProductMapList;
        }
        try {
            // 这里对列表进行反序列化需要使用TypeReference（这里在初始化objectMapper的时候就设置了capabilities的默认值）
            ArrayList<HuaweiProduct> huaweiProductList = objectMapper.readValue(jsonArray.toJSONString(), new TypeReference<ArrayList<HuaweiProduct>>() {
            });
            System.out.println();
//        List<HuaweiProduct> huaweiProductList = jsonArray.toJavaList(HuaweiProduct.class);

            for (HuaweiProduct huaweiProduct : huaweiProductList) {

                deviceProductMapPo = new SysDeviceProductMap();
                deviceTypeId = HuaweiDeviceTypeUtil.getDeviceType(huaweiProduct.getModelId(), huaweiProduct.getName());

                //TODO：产品类型等待中台更新ProductKey后进行业务调整 王伟 @since 2020-11-12
//            if(StringUtils.isEmpty(deviceTypeId)){
//                log.info("华为中台 产品品类{} {} 系统中无法找到对应设备类型",huaweiProduct.getModelId(),huaweiProduct.getName());
//                continue;
//            }
//            deviceProductMapPo.setDeviceTypeId(deviceTypeId);

                SysDeviceProductMap productMap = sysDeviceProductMapService.getOne(new LambdaQueryWrapper<SysDeviceProductMap>()
                        .eq(SysDeviceProductMap::getProductCode, huaweiProduct.getProductKey())
                        .eq(SysDeviceProductMap::getThirdPartyNo, configByProjectId.getThirdPartyNo()).last("limit 1"));
                if (productMap == null) {
                    deviceProductMapPo.setProductId(UUID.randomUUID().toString().replaceAll("-", ""));
                } else {
                    deviceProductMapPo = productMap;
                }
                deviceProductMapPo.setProductCode(huaweiProduct.getProductKey());
                deviceProductMapPo.setProductType(huaweiProduct.getProductType());
                deviceProductMapPo.setProductName(huaweiProduct.getName());
                deviceProductMapPo.setProductModel(huaweiProduct.getProductModel());
                deviceProductMapPo.setModelId(huaweiProduct.getModelId());
                deviceProductMapPo.setManufacture(huaweiProduct.getManufacturer());
                deviceProductMapPo.setProductDesc(huaweiProduct.getDesc());
                deviceProductMapPo.setProtocal(huaweiProduct.getProtocol());
                deviceProductMapPo.setDataFormat(huaweiProduct.getDataFormat());
                deviceProductMapPo.setIndustry(huaweiProduct.getIndustry());
                deviceProductMapPo.setCapabilities(huaweiProduct.getCapabilities().toString());
                deviceProductMapPo.setCapability(huaweiProduct.getCapability());
                deviceProductMapPo.setAdaptionId(huaweiProduct.getAdaptionId());
                deviceProductMapPo.setThirdPartyNo(configByProjectId.getThirdPartyNo());//对接华为的产品，固定对接华为的接口 from dba
                deviceProductMapPo.setModelType(huaweiProduct.getModelType());
//            deviceProductMapPo.setProjectId(projectId);

                deviceProductMapList.add(deviceProductMapPo);
            }

        } catch (JsonProcessingException e) {
            log.error("[华为中台] 产品同步异常");
            e.printStackTrace();
        }

        return deviceProductMapList;
    }

    private boolean addDeviceToDeviceGroup(List<String> devIdList) {
        String deviceGroupId = this.getDeviceGroupId(ProjectContextHolder.getProjectId(), TenantContextHolder.getTenantId());
        if (CollUtil.isNotEmpty(devIdList)) {
            Integer projectId = ProjectContextHolder.getProjectId();
            boolean result = this.addDeviceToDeviceGroup(deviceGroupId, devIdList, ProjectContextHolder.getProjectId(), TenantContextHolder.getTenantId());
            if (!result) {
                throw new RuntimeException("添加到设备组失败");
            }
        }
        return true;
    }

    private boolean removeDeviceFromDeviceGroup(List<String> devIdList) {
        String deviceGroupId = this.getDeviceGroupId(ProjectContextHolder.getProjectId(), TenantContextHolder.getTenantId());
        if (CollUtil.isNotEmpty(devIdList)) {
            boolean result = this.removeDeviceFromDeviceGroup(deviceGroupId, devIdList, ProjectContextHolder.getProjectId(), TenantContextHolder.getTenantId());
            if (!result) {
                throw new RuntimeException("设备从设备组中删除失败");
            }
        }
        return true;
    }


    /**
     * <p>
     * 获取当前项目的设备组ID（如果一开始设备组ID第三方编码就是错的，就会有问题）
     * </p>
     *
     * @param projectId 项目ID
     * @author: 王良俊
     */
    public String getDeviceGroupId(Integer projectId, Integer tenantId) {
        ProjectInfo projectInfo = projectInfoService.getOne(new LambdaUpdateWrapper<ProjectInfo>().eq(ProjectInfo::getProjectId, projectId));
        String groupId = projectInfo.getThirdPartyNo();
        // 如果没有设备组ID说明还未创建这个项目的设备组这里请求接口创建设备组
        if (StrUtil.isEmpty(groupId)) {
            groupId = this.addDeviceGroup(projectId.toString(), projectId, tenantId);
            projectInfo.setThirdPartyNo(groupId);
            projectInfoService.updateById(projectInfo);
        }
        return groupId;
    }


    /**
     * <p>
     * 创建设备组
     * </p>
     *
     * @param groupName 所要创建的设备组名（一般就是项目ID之类的）
     * @param projectId 设备组归属项目ID
     * @return 设备组ID
     * @author: 王良俊
     */
    public String addDeviceGroup(String groupName, Integer projectId, Integer tenantId) {
       /* log.info("[华为中台] 创建设备组 项目ID：{}", projectId);
        HuaweiConfigDTO configDTO = sysDeviceTypeThirdPartyConfigService.getConfigByProjectId(projectId, tenantId, HuaweiConfigDTO.class);
        HuaweiRespondDTO huaweiRespondDTO = HuaweiRemoteDeviceGroupServiceFactory.getInstance(getVer()).addDeviceGroup(configDTO, groupName);
        JSONObject bodyObj = huaweiRespondDTO.getBodyObj();

        // 这里需要把项目中所有设备都添加到设备组中（因为设备组之前没有创建，所以所有设备都没有存放在设备组中）
        List<ProjectDeviceInfo> deviceInfoList = projectDeviceInfoService.list(new LambdaQueryWrapper<ProjectDeviceInfo>().eq(ProjectDeviceInfo::getProjectId, projectId)
                .isNotNull(ProjectDeviceInfo::getThirdpartyCode));
        String groupId = HuaweiErrorEnum.SUCCESS.equals(huaweiRespondDTO.getErrorEnum()) ? bodyObj.getString("group_id") : null;
        log.info("[华为中台] 获取到项目：{}的设备组ID：{}", projectId, groupId);
        if (CollUtil.isNotEmpty(deviceInfoList)) {
            // 这里获取到项目中所有设备的第三方ID列表
            List<String> thirdPartyCodeList = deviceInfoList.stream().map(ProjectDeviceInfo::getThirdpartyCode).collect(Collectors.toList());
            boolean deviceAddResult = this.addDeviceToDeviceGroup(groupId, thirdPartyCodeList, projectId, tenantId);
            if (!deviceAddResult) {
                log.error("[华为中台] 自动添加设备组设备添加失败 项目ID：{} 设备组ID：{}", projectId, groupId);
                // 这里因为添加失败所以不允许保存设备组ID，否则会造成旧设备无法自动添加到设备组
                groupId = null;
            }
        }
        return groupId;*/
        return null;
    }


    /**
     * <p>
     * 添加设备到设备组
     * </p>
     *
     * @param devIdList 设备ID列表(第三方设备ID，非系统设备ID)
     * @param groupId   设备组的ID
     * @param projectId 设备组归属项目ID
     * @author: 王良俊
     */
    public boolean addDeviceToDeviceGroup(String groupId, List<String> devIdList, Integer projectId, Integer tenantId) {
      /*  HuaweiConfigDTO configDTO = sysDeviceTypeThirdPartyConfigService.getConfigByProjectId(projectId, tenantId, HuaweiConfigDTO.class);
        HuaweiRespondDTO respondDTO = HuaweiRemoteDeviceGroupServiceFactory.getInstance(getVer())
                .deviceGroupManager(configDTO, groupId, devIdList, "addDevice");
        boolean result = HuaweiErrorEnum.SUCCESS.equals(respondDTO.getErrorEnum());
        if (!result) {
            log.error("[华为中台] 设备添加到设备组失败 项目ID：{} 设备第三方ID：{}", projectId, devIdList.toString());
        }
        // 说明设备组ID是错误的需要重新获取正确的设备组ID并更新到项目信息表
        if (HuaweiErrorEnum.NO_GROUP.equals(respondDTO.getErrorEnum())) {
            // 重新获取正确的设备组ID，并重新进行设备添加操作
            String thirdPartyNo = this.addDeviceGroup(projectId.toString(), projectId, tenantId);
            projectInfoService.update(new LambdaUpdateWrapper<ProjectInfo>().eq(ProjectInfo::getProjectId, projectId).set(ProjectInfo::getThirdPartyNo, thirdPartyNo));
            // 这里主要是获取单台设备的
            result = this.addDeviceToDeviceGroup(thirdPartyNo, devIdList, projectId, tenantId);
        }
        return result;*/
        return true;
    }

    /**
     * <p>
     * 删除设备组设备
     * </p>
     *
     * @param devIdList 设备ID列表(第三方设备ID，非系统设备ID)
     * @param groupId   设备组的ID
     * @param projectId 设备组归属项目ID
     * @author: 王良俊
     */
    public boolean removeDeviceFromDeviceGroup(String groupId, List<String> devIdList, Integer projectId, Integer tenantId) {
       /* HuaweiConfigDTO configDTO = sysDeviceTypeThirdPartyConfigService.getConfigByProjectId(projectId, tenantId, HuaweiConfigDTO.class);
        HuaweiRespondDTO respondDTO = HuaweiRemoteDeviceGroupServiceFactory.getInstance(getVer()).deviceGroupManager(configDTO, groupId, devIdList, "removeDevice");
        // 说明设备组ID是错误的需要重新获取正确的设备组ID并更新到项目信息表
        boolean result = HuaweiErrorEnum.SUCCESS.equals(respondDTO.getErrorEnum());
        if (!result) {
            log.error("[华为中台] 设备从设备组删除失败 项目ID：{} 设备第三方ID：{}", projectId, devIdList.toString());
        }
        // 说明设备组ID是错误的需要重新获取正确的设备组ID并更新到项目信息表
        if (HuaweiErrorEnum.NO_GROUP.equals(respondDTO.getErrorEnum())) {
            // 重新获取正确的设备组ID，并重新进行设备删除操作
            String thirdPartyNo = this.addDeviceGroup(projectId.toString(), projectId, tenantId);
            projectInfoService.update(new LambdaUpdateWrapper<ProjectInfo>().eq(ProjectInfo::getProjectId, projectId).set(ProjectInfo::getThirdPartyNo, thirdPartyNo));
            result = this.removeDeviceFromDeviceGroup(thirdPartyNo, devIdList, projectId, tenantId);
        }
        return result;*/
        return true;
    }


    /**
     * <p>
     * 查询设备组设备
     * </p>
     * json内容
     * {
     * "total": 1,
     * "pageNo": 1,
     * "pageSize": 49,
     * "marker": "60af0d5db2267a0257cc21d2",
     * "list": [
     * {
     * "devId": "5fe40e5e5c886202ce5ff5a8_3G2H0000032T",
     * "deviceNo": "3G2H0000032T"
     * }
     * ]
     * }
     *
     * @param pageNo    页码
     * @param pageSize  分页大小
     * @param projectId 设备组归属项目ID
     * @param groupId   设备组的ID
     * @author: 王良俊
     */
    public JSONObject pageDeviceGroupDevice(String groupId, Integer pageNo, Integer pageSize, String marker, Integer projectId, Integer tenantId) {
       /* HuaweiConfigDTO configDTO = sysDeviceTypeThirdPartyConfigService.getConfigByProjectId(projectId, tenantId, HuaweiConfigDTO.class);
        HuaweiRespondDTO respondDTO = HuaweiRemoteDeviceGroupServiceFactory.getInstance(getVer()).queryDeviceGroup(configDTO, groupId, pageNo, pageSize, marker);
        return respondDTO.getBodyObj();*/
        return new JSONObject();
    }

    @Override
    public boolean setDeviceParameters(ObjectNode paramsNode, String deviceId, String thirdpartyCode) {
        if (StringUtils.isEmpty(thirdpartyCode)) {
            log.error("[华为中台] 设备ID：{} 无第三方编号，跳过更新流程", deviceId);
            return false;
        }

        HuaweiConfigDTO config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceId(deviceId, HuaweiConfigDTO.class);
        ArrayNode propertiesNode = objectMapper.createArrayNode();
        Iterator<Map.Entry<String, JsonNode>> fields = paramsNode.fields();

        fields.forEachRemaining(paramItem -> {
            /*
             *  这里会生成如下格式json
             *  {
             *      "serviceId": "DeviceParam",
             *      "propertieTag": "faceParam",
             *      "value": {
             *          "securityLevel": 2,
             *          "livenessEnable": 0,
             *          "faceEnable": 1,
             *          "inductionEnable": 0
             *      }
             *  }
             *
             */
            ObjectNode paramNode = objectMapper.createObjectNode();
            paramNode.put("serviceId", "DeviceParam");
            paramNode.put("propertieTag", paramItem.getKey());
            paramNode.set("value", paramItem.getValue());
            propertiesNode.add(paramNode);
        });
        log.info("[华为中台] 本次要进行设置的参数JSON：{}", propertiesNode);

        HuaweiRespondDTO huaweiRespondDTO = HuaweiRemoteDeviceOperateServiceFactory.getInstance(getVer()).propertiesDown(config, thirdpartyCode, "",
                JSON.parseArray(propertiesNode.toString()), null);
        return HuaweiErrorEnum.SUCCESS.code.equals(huaweiRespondDTO.getErrorEnum().code);
    }

    @Override
    public boolean cleanMediaAd(Long adSeq, String deviceId) {
        return false;
    }

    @Override
    public boolean setAccount(ProjectDeviceInfoProxyVo device) {
        return false;
    }

    @Override
    public boolean setGatherAlarmRule(ProjectDeviceInfoProxyVo device, ProjectDeviceInfoProxyVo parDevice, ProjectDeviceGatherAlarmRuleVo rule) {
        return false;
    }

    @Override
    public AurineEdgeDeviceInfoDTO getChannel(ProjectDeviceInfoProxyVo deviceInfo) {
        return null;
    }

    @Override
    public boolean operateFloor(String deviceId, String[] floors) {
        return false;
    }

    /**
     * 获取版本
     *
     * @return
     */
    @Override
    public String getVersion() {
        return VersionEnum.V1.code;
    }

    @Override
    public boolean regDevice(DeviceRegDto dto) {
        return false;
    }

    @Override
    public boolean statusChange(DeviceStatusDto dto) {
        return false;
    }

    @Override
    public boolean configDefaultParam(String deviceId) {
        return false;
    }

    @Override
    public boolean openAlways(String deviceId, Integer doorAction) {
        return false;
    }

    /**
     * 获取版本
     *
     * @return
     */
    public VersionEnum getVer() {
        return VersionEnum.V1;
    }

    /**
     * 获取平台类型
     *
     * @return
     */
    @Override
    public String getPlatform() {
        return PlatformEnum.HUAWEI_MIDDLE.code;
    }

}
