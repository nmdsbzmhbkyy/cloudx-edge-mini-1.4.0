package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.param.entity;

import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.param.entity.base.BaseIotControl;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 路灯的设备状态信息服务（控制开关灯等）
 * </p>
 *
 * @author : 王良俊
 * @date : 2021-08-10 14:55:13
 */
@Data
@NoArgsConstructor
public class StreetLightDeviceStatus extends BaseIotControl implements Serializable {

    public StreetLightDeviceStatus(String lightingStatus, String brightness, String deviceId) {
        this.lightingStatus = lightingStatus;
        this.brightness = brightness;
        this.deviceId = deviceId;
    }

    public StreetLightDeviceStatus(String lightingStatus, String brightness, List<String> deviceIdList) {
        this.lightingStatus = lightingStatus;
        this.brightness = brightness;
        this.deviceIdList = deviceIdList;
    }

    /**
     * 照明状态 1:开,0:关
     */
    private String lightingStatus;

    /**
     * 亮度 %
     */
    private String brightness;

    /**
     * 电压 mV
     */
    @JsonIgnore
    private String voltage;

    /**
     * 电流 mA
     */
    @JsonIgnore
    private String electricity;

    /**
     * 功率 W
     */
    @JsonIgnore
    private String power;

    /**
    * <p>
    * 获取路灯控制所需要的参数数据
    * [
    *     {
    *         "serviceId":"DeviceStatus",
    *         "propertieTag":"lightingStatus",
    *         "value":"1"
    *     },
    *     Object{...},
    *     Object{...},
    *     Object{...},
    *     Object{...}
    * ]
    * </p>
    *
    * @return 属性设置所需要的properties
    */
    @JsonIgnore
    public ArrayNode getProperties() {
        ArrayNode arrayNode = objectMapper.createArrayNode();
        if (StrUtil.isNotEmpty(lightingStatus)) {
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("serviceId", "DeviceStatus");
            objectNode.put("propertieTag", "lightingStatus");
            objectNode.put("value", lightingStatus);
            arrayNode.add(objectNode);
        }
        if (StrUtil.isNotEmpty(brightness)) {
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("serviceId", "DeviceStatus");
            objectNode.put("propertieTag", "brightness");
            objectNode.put("value", brightness);
            arrayNode.add(objectNode);
        }
        return arrayNode;
    }

}
