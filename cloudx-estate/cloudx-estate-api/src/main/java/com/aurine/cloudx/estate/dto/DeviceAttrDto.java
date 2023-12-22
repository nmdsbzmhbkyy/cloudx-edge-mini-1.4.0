package com.aurine.cloudx.estate.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 设备拓展字段值
 * @Author cyw
 * @Date 2023 04 23 16 40
 **/
@Data
@ApiModel(value = "设备拓展字段")
public class DeviceAttrDto {
    @ApiModelProperty(value = "设备ID")
    private String deviceId;
    @ApiModelProperty(value = "自定义字段名")
    private String attrCode;
    @ApiModelProperty(value = "自定义字段值")
    private String attrValue;
}
