package com.aurine.cloudx.estate.thirdparty.main.entity.dto;

import cn.hutool.json.JSONObject;
import lombok.Data;

/**
 * 设备状态更新通知
 */
@Data
public class EventDeviceStatusMessage {
    /**
     * 设备状态变化
     */
    private String status;
    /**
     * 设备的第三方编码
     */
    private String thirdPartyCode;
    /**
     * 设备的SN编码
     */
    private String deviceSn;
    /**
     * 设备云平台Id
     */
    private String deviceId;
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 额外消息通知
     */
    private JSONObject attr;
}
