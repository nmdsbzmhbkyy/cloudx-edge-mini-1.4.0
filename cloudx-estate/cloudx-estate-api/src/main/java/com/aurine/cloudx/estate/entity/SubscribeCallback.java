package com.aurine.cloudx.estate.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: wrm
 * @Date: 2022/01/11 17:19
 * @Package: com.aurine.cloudx.edge.sync.api.entity.open
 * @Version: 1.0
 * @Remarks:
 **/
@Data
public class SubscribeCallback {

    @ApiModelProperty(value = "回调地址")
    private String callbackUrl;

    @ApiModelProperty(value = "回调类型，0-级联入云类；1-操作类；2-指令类；3-事件类；4-反馈类；9-其他")
    private String callbackType;

    @ApiModelProperty(value = "项目UUID")
    private String projectUUID;

    @ApiModelProperty(value = "回调说明")
    private String callbackDesc;

    @ApiModelProperty(value = "回调请求头参数，参数名为Param")
    private String callbackHeaderParam;

    @ApiModelProperty(value = "回调id，uuid")
    private String callbackId;

    @ApiModelProperty(value = "项目类型，1-边缘网关；2-WR2X")
    private String projectType;
}
