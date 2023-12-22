package com.aurine.cloudx.common.core.constant.enums;

import com.aurine.cloudx.common.core.constant.DeviceTypeConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>设备类型枚举</p>
 *
 * @ClassName: DeviceTypeEnum
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-07-28 15:21
 * @Copyright:
 */
@Getter
@AllArgsConstructor
public enum DeviceTypeEnum {

    /**
     * 室内终端
     */
    INDOOR_DEVICE(DeviceTypeConstants.INDOOR_DEVICE, "室内终端"),

    /**
     * 梯口终端
     */
    LADDER_WAY_DEVICE(DeviceTypeConstants.LADDER_WAY_DEVICE, "梯口终端"),

    /**
     * 区口终端
     */
    GATE_DEVICE(DeviceTypeConstants.GATE_DEVICE, "区口终端"),

    /**
     * 中心机
     */
    CENTER_DEVICE(DeviceTypeConstants.CENTER_DEVICE, "中心机"),

    /**
     * 编码设备
     */
    ENCODE_DEVICE(DeviceTypeConstants.ENCODE_DEVICE, "编码设备"),

    /**
     * 监控设备
     */
    MONITOR_DEVICE(DeviceTypeConstants.MONITOR_DEVICE, "监控设备"),

    /**
     * 报警主机
     */
    ALARM_DEVICE(DeviceTypeConstants.ALARM_HOST, "报警主机"),

    /**
     * 智能水表
     */
    WATER_METER_DEVICE(DeviceTypeConstants.SMART_WATER_METER, "智能水表"),

    /**
     * 液位计
     */
    LEVEL_GAUGE_DEVICE(DeviceTypeConstants.LEVEL_GAUGE, "液位计"),

    /**
     * 烟感
     */
    SMOKE_DEVICE(DeviceTypeConstants.SMOKE, "烟感"),

    /**
     * 智能井盖
     */
    WELL_DEVICE(DeviceTypeConstants.SMART_MANHOLE_COVER, "智能井盖"),

    /**
     * 智能路灯
     */
    STREET_LAMP_DEVICE(DeviceTypeConstants.SMART_STREET_LIGHT, "智能路灯"),
    /**
     * 智能路灯
     */
    DEVICE_DRIVER(DeviceTypeConstants.DEVICE_DRIVER, "驱动设备"),
    /**
     * 智能路灯
     */
    SINGLE_ACCESS_DEVICE(DeviceTypeConstants.SINGLE_ACCESS_DEVICE, "单门门禁设备"),
    /**
     * 电梯分层控制器
     */
    ELEVATOR_LAYER_CONTROL_DEVICE(DeviceTypeConstants.ELEVATOR_LAYER_CONTROL_DEVICE, "电梯分层控制器"),
    /**
     * 电梯乘梯识别终端
     */
    ELEVATOR_RECOGNIZER_DEVICE(DeviceTypeConstants.ELEVATOR_RECOGNIZER_DEVICE, "电梯乘梯识别终端"),
    /**
     * 电梯状态检测器
     */
    ELEVATOR_STATE_DETECTOR_DEVICE(DeviceTypeConstants.ELEVATOR_STATE_DETECTOR_DEVICE, "电梯状态检测器"),

    /**
     * WR20网关
     */
    WR20_DEVICE("-11", "WR20网关设备"),
    /**
     * AI盒子
     */
    AI_BOX_DEVICE(DeviceTypeConstants.AI_BOX_DEVICE, "AI盒子"),
    /**
     * 未定义设备类型
     */
    OTHER("-1", "未定义设备类型");


    /**
     * 类型
     */
    private String code;

    /**
     * 名称
     */
    private String note;


    /**
     * @param code
     * @return
     */
    public static DeviceTypeEnum getByCode(String code) {
        for (DeviceTypeEnum value : DeviceTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return DeviceTypeEnum.OTHER;
    }

}
