package com.aurine.cloudx.estate.thirdparty.remote.edge.ali.entity.dto;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description: 阿里边缘网关 DTO对象
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-06-22
 * @Copyright:
 */
@Data
public class AliEdgeEventDTO {

    @ApiModelProperty(value = "主要信息")
    JSONObject onNotifyData;

    @ApiModelProperty(value = "资源：如 ali.device.event")
    String resource;
    @ApiModelProperty(value = "事件：如 report,event")
    String event;
    @ApiModelProperty(value = "订阅ID")
    String subscriptionId;

}
