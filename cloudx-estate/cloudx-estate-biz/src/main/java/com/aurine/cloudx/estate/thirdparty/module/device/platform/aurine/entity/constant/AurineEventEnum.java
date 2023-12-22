package com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.constant;

import lombok.AllArgsConstructor;

/**
 * <p>冠林中台 事件类型枚举</p>
 *
 * @ClassName: AurineEventEnum
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-19 18:00
 * @Copyright:
 */
@AllArgsConstructor
public enum AurineEventEnum {
    /**
     * 异步事件
     */
    NOTICE("0", "异步通知"),
    /**
     * 开门事件
     */
    OPEN_DOOR("1", "开门"),
    /**
     * 门禁异常事件
     */
    DOOR_ERROR("2", "门禁异常"),
    /**
     * 门禁告警事件
     */
    DOOR_WARNING("3", "门禁告警"),
    /**
     * 通话记录上报
     */
    WRONG_HEAD("4", "通话记录上报"),
    /**
     * 安防告警事件
     */
    SECURITY("5", "安防告警"),
    /**
     * 门状态事件
     */
    DOOR_STATIC("6", "门状态"),
    /**
     * 设备运行状态
     */
    DEVICE_STATIC("10005", "设备运行状态");

    public String code;
    public String desc;

}
