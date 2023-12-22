package com.aurine.cloudx.common.uniview.model;

import lombok.Data;

@Data
public class GetRecordUrlVo {
    // 设备序列号
    private String deviceSerial;
    // 通道号
    private Integer channelNo;
    // 媒体流索引
    private Integer streamIndex;
    // 媒体流类型，默认为live,取值live为直播，record为回放
    private String streamType;
    // 开始时间，UTC时间，streamType为record时必选
    private Long startTime;
    // 结束时间，UTC时间，查询时间跨度不能超过一天，streamType为record时必选
    private Long endTime;
    // 录像类型（0-全部录像，1-普通录像 2-事件录像），默认为全部录像，streamType为record时必选
    private Integer recordTypes;
    // 直播地址类型，默认值为hls， rtmp RTMP方式 hls HLS方式 FLV 方式
    private String type;
}
