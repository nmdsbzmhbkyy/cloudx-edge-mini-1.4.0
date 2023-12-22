package com.aurine.cloudx.estate.thirdparty.remote.edge.ali.entity.dto;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description: 阿里边缘网关 周界告警 DTO对象
 * {
 * "deviceType": "CustomCategory",
 * "identifier": "channelAlarm",
 * "iotId": "ZJBJ",
 * "checkFailedData": {},
 * "requestId": "65593",
 * "name": "通道告警",
 * "time": 1624340955791,
 * "productKey": "a1relni3Fx3",
 * "type": "alert",
 * "deviceName": "ZJBJ.4",
 * "value": {
 * "alarmType": 1,
 * "channelID": "Module000Zone001"
 * }
 * }
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-06-22
 * @Copyright:
 */
@Data
public class AliEdgeEventPerimeterAlarmDTO {

    @ApiModelProperty(value = "设备类型：CustomCategory")
    String deviceType;

    @ApiModelProperty(value = "事件类型：channelAlarm")
    String identifier;

    @ApiModelProperty(value = "主机SN：ZJBJ")
    String iotId;

    @ApiModelProperty(value = "请求ID：65593")
    String requestId;

    @ApiModelProperty(value = "事件名称：通道告警")
    String name;

    @ApiModelProperty(value = "时间戳：1624340955791")
    Long time;

    @ApiModelProperty(value = "事件类型：alert")
    String type;

    @ApiModelProperty(value = "设备名称：ZJBJ.4")
    String deviceName;

    @ApiModelProperty(value = "checkFailedData")
    JSONObject checkFailedData;

    @ApiModelProperty(value = "报警值   \"alarmType\": 1,  \"channelID\": \"Module000Zone001\"")
    JSONObject value;


}
