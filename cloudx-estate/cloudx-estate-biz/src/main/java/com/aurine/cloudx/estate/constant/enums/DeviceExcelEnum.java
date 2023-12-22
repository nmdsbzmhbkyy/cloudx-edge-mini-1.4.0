package com.aurine.cloudx.estate.constant.enums;

import com.aurine.cloudx.common.core.constant.DeviceTypeConstants;
import com.aurine.cloudx.estate.excel.CodeDeviceExcel;
import com.aurine.cloudx.estate.excel.entity.access.*;
import com.aurine.cloudx.estate.excel.entity.iot.SmokeAndWaterExcel;
import com.aurine.cloudx.estate.excel.*;

import lombok.AllArgsConstructor;
import lombok.Getter;

//import com.aurine.cloudx.estate.excel.entity.iot.GaugeAndManholeCoverExcel;

/**
 * (DeviceExcelEnum)设备模板路径枚举
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/8/17 16:22
 */
@Getter
@AllArgsConstructor
public enum DeviceExcelEnum {


    /**
     * 室内终端(系统)
     */
    INDOOR_DEVICE_SYSTEM(DeviceTypeConstants.INDOOR_DEVICE, "0", "室内终端—系统标准.xlsx", IndoorDeviceExcel.class),

    /**
     * 室内终端(公安)
     */
    INDOOR_DEVICE_PUBLIC(DeviceTypeConstants.INDOOR_DEVICE, "1", "室内终端—公安标准.xlsx", IndoorDeviceExcel.class),

    /**
     * 梯口终端(系统)
     */
    LADDER_WAY_DEVICE_SYSTEM(DeviceTypeConstants.LADDER_WAY_DEVICE, "0", "梯口终端—系统标准.xlsx", LadderDeviceExcel.class),
    /**
     * 梯口终端(公安)
     */
    LADDER_WAY_DEVICE_PUBLIC(DeviceTypeConstants.LADDER_WAY_DEVICE, "1", "梯口终端—公安标准.xlsx", LadderDeviceExcel.class),

    /**
     * 区口终端(系统)
     */
    GATE_DEVICE_SYSTEM(DeviceTypeConstants.GATE_DEVICE, "0", "区口终端—系统标准.xlsx", GateAndCenterDeviceExcel.class),
    /**
     * 区口终端(公安)
     */
    GATE_DEVICE_PUBLIC(DeviceTypeConstants.GATE_DEVICE, "1", "区口终端—公安标准.xlsx", GateAndCenterDeviceExcel.class),

    /**
     * 中心机(系统)
     */
    CENTER_DEVICE_SYSTEM(DeviceTypeConstants.CENTER_DEVICE, "0", "中心机—系统标准.xlsx", GateAndCenterDeviceExcel.class),
    /**
     * 中心机(公安)
     */
    CENTER_DEVICE_PUBLIC(DeviceTypeConstants.CENTER_DEVICE, "1", "中心机—公安标准.xlsx", GateAndCenterDeviceExcel.class),

    /**
     * 编码设备(系统)
     */
    ENCODE_DEVICE_SYSTEM(DeviceTypeConstants.ENCODE_DEVICE, "0", "编码设备—系统标准.xlsx", CodeDeviceExcel.class),
    /**
     * 编码设备(公安)
     */
    ENCODE_DEVICE_PUBLIC(DeviceTypeConstants.ENCODE_DEVICE, "1", "编码设备—公安标准.xlsx", CodeDeviceExcel.class),

    /**
     * 车辆道闸一体机(系统)
     */
    VEHICLE_BARRIER_DEVICE_SYSTEM(DeviceTypeConstants.VEHICLE_BARRIER_DEVICE, "0", "车辆道闸一体机—系统标准.xlsx", VehicleBarrierDeviceExcel.class),
    /**
     * 车辆道闸一体机(公安)
     */
    VEHICLE_BARRIER_DEVICE_PUBLIC(DeviceTypeConstants.VEHICLE_BARRIER_DEVICE, "1", "车辆道闸一体机—公安标准.xlsx", VehicleBarrierDeviceExcel.class),

    /**
     * 电梯分层控制器(系统)
     */
    ELEVATOR_LAYER_CONTROL_DEVICE_SYSTEM(DeviceTypeConstants.ELEVATOR_LAYER_CONTROL_DEVICE,"0", "电梯分层器—系统标准.xlsx", ElevatorDeviceExcel.class),
    /**
     * 电梯分层控制器(公安)
     */
    ELEVATOR_LAYER_CONTROL_DEVICE_PUBLIC(DeviceTypeConstants.ELEVATOR_LAYER_CONTROL_DEVICE,"1", "电梯分层器—公安标准.xlsx", ElevatorDeviceExcel.class),

    /**
     * 电梯乘梯识别终端(系统)
     */
    ELEVATOR_RECOGNIZER_DEVICE_SYSTEM(DeviceTypeConstants.ELEVATOR_RECOGNIZER_DEVICE,"0", "乘梯识别终端—系统标准.xlsx", ElevatorDeviceExcel.class),
    /**
     * 电梯乘梯识别终端(公安)
     */
    ELEVATOR_RECOGNIZER_DEVICE_PUBLIC(DeviceTypeConstants.ELEVATOR_RECOGNIZER_DEVICE,"1", "乘梯识别终端—公安标准.xlsx", ElevatorDeviceExcel.class),

    /**
     * 电梯状态检测器(系统)
     */
    ELEVATOR_STATE_DETECTOR_DEVICE_SYSTEM(DeviceTypeConstants.ELEVATOR_STATE_DETECTOR_DEVICE,"0", "电梯状态检测器—系统标准.xlsx", ElevatorStateDetectorDeviceExcel.class),
    /**
     * 电梯状态检测器(公安)
     */
    ELEVATOR_STATE_DETECTOR_DEVICE_PUBLIC(DeviceTypeConstants.ELEVATOR_STATE_DETECTOR_DEVICE,"1", "电梯状态检测器—公安标准.xlsx", ElevatorStateDetectorDeviceExcel.class),

    /*----------------------------------------IOT设备--------------------------------------------------------*/

    /**
     * 智能井盖(系统)
     */
//    SMART_MANHOLE_COVER_SYSTEM(DeviceTypeConstants.SMART_MANHOLE_COVER, "0","智能井盖—系统标准.xlsx", GaugeAndManholeCoverExcel.class),
    /**
     * 智能井盖(公安)
     */
//    SMART_MANHOLE_COVER_PUBLIC(DeviceTypeConstants.SMART_MANHOLE_COVER, "1","智能井盖—公安标准.xlsx", GaugeAndManholeCoverExcel.class),

    /**
     * 智能水表(系统)
     */
    SMART_WATER_METER_SYSTEM(DeviceTypeConstants.SMART_WATER_METER, "0", "智能水表—系统标准.xlsx", SmokeAndWaterExcel.class),
    /**
     * 智能水表(公安)
     */
    SMART_WATER_METER_PUBLIC(DeviceTypeConstants.SMART_WATER_METER, "1", "智能水表—公安标准.xlsx", SmokeAndWaterExcel.class),

    /**
     * 智能水表(系统)
     */
//    LEVEL_GAUGE_SYSTEM(DeviceTypeConstants.LEVEL_GAUGE, "0","液位计—系统标准.xlsx", GaugeAndManholeCoverExcel.class),
    /**
     * 智能水表(公安)
     */
//    LEVEL_GAUGE_PUBLIC(DeviceTypeConstants.LEVEL_GAUGE, "1","液位计—公安标准.xlsx", GaugeAndManholeCoverExcel.class),

    /**
     * 烟感(系统)
     */
    SMOKE_SYSTEM(DeviceTypeConstants.SMOKE, "0", "烟感—系统标准.xlsx", SmokeAndWaterExcel.class),
    /**
     * 烟感(公安)
     */
    SMOKE_PUBLIC(DeviceTypeConstants.SMOKE, "1", "烟感—公安标准.xlsx", SmokeAndWaterExcel.class);


    /**
     * 设备类型
     */
    private final String code;
    /**
     * 模板类型
     */
    private final String type;

    /**
     * 文件名称
     */
    private final String name;

    /**
     * 导入模板类
     */
    private final Class<?> clazz;


    /**
     * @param code
     * @param type
     * @return
     */
    public static DeviceExcelEnum getEnum(String code, String type) {
        for (DeviceExcelEnum value : DeviceExcelEnum.values()) {
            if (value.getCode().equals(code) && value.getType().equals(type)) {
                return value;
            }
        }
        return null;
    }

}