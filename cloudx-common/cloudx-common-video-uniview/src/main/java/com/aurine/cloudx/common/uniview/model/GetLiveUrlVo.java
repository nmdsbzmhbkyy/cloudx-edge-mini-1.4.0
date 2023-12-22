package com.aurine.cloudx.common.uniview.model;

import lombok.Data;

@Data
public class GetLiveUrlVo {
    // 设备序列号
    private String deviceSerial;
    // 通道号
    private Integer channelNo;
    // 媒体流索引
    private Integer streamIndex;
    // 直播地址类型，默认值为hls， rtmp RTMP方式 hls HLS方式 FLV 方式
    private String type;
}
