package com.aurine.cloud.code.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class IdCard {


    /**
     * 身份证号
     */
    @ApiModelProperty(value = "身份证号")
    private String identityNumber;


    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String username;

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

    @ApiModelProperty(value = "身份证城市")
    private String qrCodeType;

    @ApiModelProperty(value = "版本信息")
    private String versionName;


}