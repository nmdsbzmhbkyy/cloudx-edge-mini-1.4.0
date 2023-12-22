package com.aurine.cloudx.estate.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: 顾文豪
 * @Date: 2023/11/8 17:27
 * @Package: com.aurine.openv2.dto
 * @Version: 1.0
 * @Remarks:
 **/
@Data
@ApiModel(value = "人脸黑名单状态Dto")
public class OpenApiProjectBlacklistFaceStatusDto {

    /**
     * 第三方人脸ID
     */
    @JSONField(name = "faceId")
    @JsonProperty(value = "faceId")
    @ApiModelProperty(value = "第三方人脸ID")
    private String faceId;
    /**
     * 下载状态 0 未下载 1 已下载 2 下载失败
     */
    @JSONField(name = "dlStatus")
    @JsonProperty(value = "dlStatus")
    @ApiModelProperty(value = "下载状态 0 未下载 1 已下载 2 下载失败")
    private String dlStatus;
    /**
     * 设备编码
     */
    @JSONField(name = "deviceCode")
    @JsonProperty(value = "deviceCode")
    @ApiModelProperty(value = "设备编码")
    private String deviceCode;
    /**
     * 设备id
     */
    @JSONField(name = "deviceId")
    @JsonProperty(value = "deviceId")
    @ApiModelProperty(value = "设备id")
    private String deviceId;
    /**
     * 设备名称
     */
    @JSONField(name = "deviceName")
    @JsonProperty(value = "deviceName")
    @ApiModelProperty(value = "设备名称")
    private String deviceName;

}
