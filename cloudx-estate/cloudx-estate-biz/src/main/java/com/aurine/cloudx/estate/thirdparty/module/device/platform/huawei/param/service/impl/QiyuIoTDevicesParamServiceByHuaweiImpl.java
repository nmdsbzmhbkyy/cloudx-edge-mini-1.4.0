package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.param.service.impl;

import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.ResponseOperateConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.TopicConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.dto.ResponseOperateDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.enums.DeviceManufactureEnum;
import com.aurine.cloudx.estate.vo.DeviceParamJsonVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>暂时定为适用于：智能井盖、水表、液位计的参数设置</p>
 *
 * @author : 王良俊
 * @date : 2021-07-09 11:52:47
 */
@Service
public class QiyuIoTDevicesParamServiceByHuaweiImpl extends DefaultDeviceParamServiceByHuaweiImpl {

    @Override
    protected String getLogMark() {
        return "骐驭物联网设备";
    }

    @Override
    public Set<DeviceManufactureEnum> getApplicableDeviceProducts() {
        Set<DeviceManufactureEnum> deviceManufactureEnums = new HashSet<>();
        deviceManufactureEnums.add(DeviceManufactureEnum.QIYU_SMOKE);
        deviceManufactureEnums.add(DeviceManufactureEnum.QIYU_SMART_STREET_LIGHT);
        return deviceManufactureEnums;
    }

    @Override
    public void deviceDataUpdate(String json, ProjectDeviceInfo deviceInfo) {
        log.info("[华为中台] 开始处理骐驭设备回调 json:{} deviceInfo：{}", json, deviceInfo);
        try {
            JsonNode rootNode = objectMapper.readTree(json);
            JsonNode serviceId = rootNode.findPath("serviceId");
            if (!serviceId.isMissingNode()) {
                if ("DeviceStatus".equals(serviceId.asText())) {
                    JsonNode data = rootNode.findPath("data");
                    if (data.isMissingNode()) {
                        return;
                    }
                    ObjectNode objectNode = objectMapper.createObjectNode();
                    objectNode.set("LightDeviceStatus", data);

                    ResponseOperateDTO responseOperateDTO = new ResponseOperateDTO();
                    responseOperateDTO.setCompany(PlatformEnum.HUAWEI_MIDDLE.value);
                    responseOperateDTO.setThirdPartyCode(rootNode.findPath("devId").asText());

                    responseOperateDTO.setDeviceParamJsonVo(new DeviceParamJsonVo("LightDeviceStatus", data.toString()));
                    // 设置行为未修改其他参数
                    responseOperateDTO.setAction(ResponseOperateConstant.ACTION_UPDATE_DEVICE_PARAM_IOT);
                    log.info("发送参数数据：{}", responseOperateDTO.getDeviceParamJsonVo());
                    // 再次发送消息
                    sendMsg(TopicConstant.SDI_RESPONSE_OPERATE, responseOperateDTO);
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
