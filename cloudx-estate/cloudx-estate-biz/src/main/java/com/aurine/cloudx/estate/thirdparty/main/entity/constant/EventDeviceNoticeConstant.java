package com.aurine.cloudx.estate.thirdparty.main.entity.constant;

import lombok.Data;

/**
 * @description: 设备事件通知
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-06
 * @Copyright:
 */
@Data
public class EventDeviceNoticeConstant  {
    /**
     * 设备在线状态变化
     */
    public static final String ACTION_DEVICE_STATUS_UPDATE = "DEVICE_STATUS_UPDATE";

    /**
     * 通话记录事件
     */
    public static final String ACTION_DEVICE_CALL_EVENT = "ACTION_DEVICE_CALL_EVENT";

}
