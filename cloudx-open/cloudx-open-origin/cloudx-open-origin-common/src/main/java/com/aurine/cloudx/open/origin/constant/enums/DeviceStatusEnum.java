package com.aurine.cloudx.open.origin.constant.enums;

import lombok.AllArgsConstructor;

/**
 * <p>设备状态枚举</p>
 *
 * @ClassName: DeviceStatusEnum
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-07 10:49
 * @Copyright:
 */
@AllArgsConstructor
public enum DeviceStatusEnum {

    /**
     * 在线
     */
    ONLINE("1"),

    /**
     * 离线
     */
    OFFLINE("2"),

    /**
     * 故障
     */
    ERROR("3"),

    /**
     * 未激活
     */
    DEACTIVE("4"),

    /**
     * 未知
     */
    UNKNOW("9");

    public String code;
}
