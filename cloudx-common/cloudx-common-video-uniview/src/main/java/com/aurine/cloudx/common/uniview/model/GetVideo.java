package com.aurine.cloudx.common.uniview.model;

import lombok.Data;

/**
 * 获取视频流
 */
@Data
public class GetVideo {
    // RTMP直播地址
    private String rtmpUrl;
    // HLS直播地址
    private String hlsUrl;
    // FLV直播地址
    private String flvUrl;
    // 直播状态（0-推流中 1-停止 2-错误）
    private Integer status;
}
