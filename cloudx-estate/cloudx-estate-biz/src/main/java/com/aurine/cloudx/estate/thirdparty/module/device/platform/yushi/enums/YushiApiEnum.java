package com.aurine.cloudx.estate.thirdparty.module.device.platform.yushi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpMethod;

@Getter
@AllArgsConstructor
public enum YushiApiEnum {


    SYSTEM_DEVICE_INFO("/LAPI/V1.0/System/DeviceInfo", HttpMethod.GET, "设备信息"),

    EVENT_SUBSCRIPTION("/LAPI/V1.0/System/Event/Subscription", HttpMethod.POST, "订阅告警事件"),

    EVENT_SUBSCRIPTION_REFRESH("/LAPI/V1.0/System/Event/Subscription", HttpMethod.PUT, "刷新订阅告警事件"),

    EVENT_SUBSCRIPTION_DELETE("/LAPI/V1.0/System/Event/Subscription", HttpMethod.DELETE, "删除订阅告警事件"),

    VIDEO_LIVE_STREAM("/LAPI/V1.0/Channels/1/Media/Video/Streams/1/LiveStreamURL", HttpMethod.GET, "获取指定视频通道的某视频流的实况URL");

    private final String api;

    private final HttpMethod method;

    private final String desc;
}
