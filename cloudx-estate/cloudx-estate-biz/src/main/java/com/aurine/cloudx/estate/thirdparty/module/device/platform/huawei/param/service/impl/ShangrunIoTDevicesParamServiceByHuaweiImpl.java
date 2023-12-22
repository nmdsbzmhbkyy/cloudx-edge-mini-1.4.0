package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.param.service.impl;

import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.enums.DeviceManufactureEnum;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>暂时定为适用于：智能井盖、水表、液位计的参数设置</p>
 * @author : 王良俊
 * @date : 2021-07-09 11:52:47
 */
@Service
public class ShangrunIoTDevicesParamServiceByHuaweiImpl extends DefaultDeviceParamServiceByHuaweiImpl {

    @Override
    protected String getLogMark() {
        return "上润物联网设备";
    }

    @Override
    public Set<DeviceManufactureEnum> getApplicableDeviceProducts() {
        Set<DeviceManufactureEnum> deviceManufactureEnums = new HashSet<>();
        deviceManufactureEnums.add(DeviceManufactureEnum.SHANGRUN_LEVEL_GAUGE);
        deviceManufactureEnums.add(DeviceManufactureEnum.SHANGRUN_SMART_MANHOLE_COVER);
        deviceManufactureEnums.add(DeviceManufactureEnum.SHANGRUN_SMART_WATER_METER);
        return deviceManufactureEnums;
    }

    @Override
    public void deviceDataUpdate(String json, ProjectDeviceInfo deviceInfo) {
        /*// 获取到整个json的根节点
        JsonNode rootNode = null;
        try {
            rootNode = objectMapper.readTree(json);
            // 这里获取到msgId（虽然好像没什么用）
            String msgId = rootNode.findPath("msgId").asText();
            // 第三方设备编码 可通过'_'字符分割出设备sn码
            String thirdPartyCode = rootNode.findPath("devId").asText();
            ArrayNode servicesDataArr = (ArrayNode) rootNode.findPath("servicesData");

            ResponseOperateDTO responseOperateDTO = new ResponseOperateDTO();
            responseOperateDTO.setCompany(PlatformEnum.HUAWEI_MIDDLE.value);
            JsonNode serviceId = rootNode.findPath("serviceId");
            servicesDataArr.forEach(item -> {
                JsonNode data = item.path("data");
                // 这里把参数的json数据放入dto中(这里把paramKey转换成serviceId)
                responseOperateDTO.setThirdPartyCode(thirdPartyCode);
                responseOperateDTO.setDeviceParamJsonVo(new DeviceParamJsonVo(DeviceParamEnum.getObjName(serviceId.asText()), data.toString()));
                // 设置行为未修改其他参数
                responseOperateDTO.setAction(ResponseOperateConstant.ACTION_UPDATE_DEVICE_PARAM_IOT);
                log.info("发送参数数据：{}", responseOperateDTO.getDeviceParamJsonVo());
                // 再次发送消息
                sendMsg(TopicConstant.SDI_RESPONSE_OPERATE, responseOperateDTO);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void requestDeviceParam(Set<String> serviceIdSet, String deviceId) {

    }
}
