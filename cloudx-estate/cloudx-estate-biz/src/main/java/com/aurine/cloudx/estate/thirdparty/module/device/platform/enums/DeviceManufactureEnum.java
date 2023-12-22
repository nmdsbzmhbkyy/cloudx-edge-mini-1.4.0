package com.aurine.cloudx.estate.thirdparty.module.device.platform.enums;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.common.core.constant.DeviceTypeConstants;
import lombok.AllArgsConstructor;

/**
 * <p>
 * 设备厂商和设备类型的枚举
 * </p>
 * @author : 王良俊
 * @date : 2021-07-08 10:23:47
 */
@AllArgsConstructor
public enum DeviceManufactureEnum {

    MILI_LADDER_WAY_DEVICE("米立", DeviceTypeConstants.LADDER_WAY_DEVICE, Base64.encode("米立梯口机")),
    MILI_GATE_DEVICE("米立", DeviceTypeConstants.GATE_DEVICE, Base64.encode("米立区口机")),

    MILI_DRIVER_DEVICE_EDGE("米立", DeviceTypeConstants.DEVICE_DRIVER, Base64.encode("米立边缘网关驱动设备")),

    MILI_ELEVATOR_LAYER_CONTROL_DEVICE("米立", DeviceTypeConstants.ELEVATOR_LAYER_CONTROL_DEVICE, Base64.encode("米立电梯分层控制器")),


    SHANGRUN_SMART_MANHOLE_COVER("上润", DeviceTypeConstants.SMART_MANHOLE_COVER, Base64.encode("上润智能井盖")),
    SHANGRUN_LEVEL_GAUGE("上润", DeviceTypeConstants.LEVEL_GAUGE, Base64.encode("上润液位计")),
    SHANGRUN_SMART_WATER_METER("上润", DeviceTypeConstants.SMART_WATER_METER, Base64.encode("上润智能水表")),

    QIYU_SMOKE("骐驭", DeviceTypeConstants.SMOKE, Base64.encode("骐驭烟感")),
    QIYU_SMART_STREET_LIGHT("骐驭", DeviceTypeConstants.SMART_STREET_LIGHT, Base64.encode("骐驭智能路灯")),

    MILI_VEHICLE_BARRIER_DEVICE("赛菲姆", DeviceTypeConstants.VEHICLE_BARRIER_DEVICE, Base64.encode("赛菲姆车辆道闸一体机")),

    DEFAULT_DEVICE("未知厂商", "未知设备类型", null);

    /**
     * 设备厂商名
     * */
    public String manufacture;

    /**
     * 设备类型ID
     * */
    public String deviceType;

    /**
     * 这种设备的唯一code
     * */
    public String code;

    public static DeviceManufactureEnum getByManufactureAndDeviceType(String manufacture, String deviceType) {
        if (StrUtil.isNotEmpty(manufacture) && StrUtil.isNotEmpty(deviceType)) {
            for (DeviceManufactureEnum value : DeviceManufactureEnum.values()) {
                if (value.manufacture.equals(manufacture) && value.deviceType.equals(deviceType)) {
                    return value;
                }
            }
        }
        // 没有录入枚举类的设备统一返回这个
        return DeviceManufactureEnum.DEFAULT_DEVICE;
    }
}
