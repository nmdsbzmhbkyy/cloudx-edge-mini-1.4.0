package com.aurine.cloud.code.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QrCard {


    /**
     * 二维码值
     */
    @ApiModelProperty(value = "二维码值")
    private String qrString;
    /**
     * 体温
     */
    @ApiModelProperty(value = "体温")
    private String bodyTemperature;
    /**
     * 设备编码
     */
    @ApiModelProperty(value = "设备编码")
    private String userMobile;
    /**
     * 二维码类型
     */
    @ApiModelProperty(value = "二维码类型 城市名")
    private String qrCodeType;

    /**
     * 版本名
     */
    @ApiModelProperty(value = "版本名")
    private String versionName;
}