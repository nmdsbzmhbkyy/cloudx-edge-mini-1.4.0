package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.enums;

import com.aurine.cloudx.common.core.constant.DeviceTypeConstants;
import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>产品设备类型枚举</p>
 * @author : 王良俊
 * @date : 2021-09-17 16:55:22
 */
@AllArgsConstructor
public enum ProductTypeEnum {

    /*********************可视对讲类************************/

    /**
     * 室内终端
     */
    INDOOR_DEVICE("020301", set(DeviceTypeConstants.INDOOR_DEVICE)),
    /**
     * 区口机和梯口机
     */
    GATE_PUBLIC_DEVICE("020302", set(DeviceTypeConstants.LADDER_WAY_DEVICE, DeviceTypeConstants.GATE_DEVICE)),
    /**
     * 门前机
     */
    DOOR_DEVICE("020307", set()),
    /**
     * 管理员机
     */
    ADMINISTRATOR_DEVICE("020399", set(DeviceTypeConstants.CENTER_DEVICE)),
    /**
     * 边缘网关
     */
    EDGE_DEVICE("020320", set(DeviceTypeConstants.DEVICE_DRIVER)),

    /*********************视频监控类************************/

    /**
     * IPC摄像头
     */
    IPC_CAMERA("020201", set(DeviceTypeConstants.MONITOR_DEVICE)),
    /**
     * 带云台摄像头
     */
    YUNTAI_CAMERA("020202", set(DeviceTypeConstants.MONITOR_DEVICE)),
    /**
     * NVR类产品
     */
    NVR("020203", set()),
    /**
     * AI盒子
     */
    AI_BOX("020204", set(DeviceTypeConstants.AI_BOX_DEVICE)),

    /*********************安全报警类************************/

    /**
     * 烟感
     */
    SMOKE_DEVICE("020101", set(DeviceTypeConstants.SMOKE)),
    /**
     * 周界主机
     */
    ALARM_HOST("020199", set(DeviceTypeConstants.ALARM_HOST)),

    /*********************车辆通行类************************/

    /**
     * 车辆道闸
     */
    VEHICLE_BARRIER("020401", set()),

    /**
     * 电梯分层控制器
     */
    ELEVATOR_FLOOR_CONTROLLER_DEVICE("020501", set(DeviceTypeConstants.ELEVATOR_LAYER_CONTROL_DEVICE)),

    /**
     * 电梯分层控制器
     */
    ELEVATOR_RECOGNITION_DEVICE("020502", set(DeviceTypeConstants.ELEVATOR_RECOGNIZER_DEVICE)),

    /*********************监测类************************/

    /**
     * 环境监测
     */
    ENVIRONMENTAL_MONITORING("030101", set()),
    /**
     * 井下液位计
     */
    LEVEL_GAUGE("030102", set(DeviceTypeConstants.LEVEL_GAUGE)),
    /**
     * 路面积水仪
     */
    ROAD_AREA_WATER_METER("030103", set()),
    /**
     * 液压检测
     */
    HYDRAULIC_TESTING("030104", set()),
    /**
     * 智能井盖（井盖监控器）
     */
    SMART_MANHOLE_COVER("030105", set(DeviceTypeConstants.SMART_MANHOLE_COVER)),
    /**
     * 地磁
     */
    GEOMAGNETISM("030106", set()),

    /*********************公共服务杂类************************/

    /**
     * 垃圾分类箱
     */
    GARBAGE_SORTING_BIN("030001", set()),
    /**
     * 充电桩
     */
    CHARGING_PILE("030010", set()),
    /**
     * 智能水表
     */
    SMART_WATER_METER("030020", set(DeviceTypeConstants.SMART_WATER_METER)),
    /**
     * 智慧路灯
     */
    SMART_STREET_LIGHT("030301", set(DeviceTypeConstants.SMART_STREET_LIGHT)),
    /**
     * 消防栓
     */
    FIRE_HYDRANT("030201", set()),

    ;

    public String modelType;
    public Set<String> deviceTypeList;

    /**
     * <p>根据产品品类类型获取对应设备类型ID</p>
     *
     * @param modelType 产品品类类型
     * @return 设备类型ID集合
     * @author: 王良俊
     */
    public static Set<String> getDeviceTypeListByModelType(String modelType) {
        for (ProductTypeEnum value : ProductTypeEnum.values()) {
            if (value.modelType.equals(modelType)) {
                return value.deviceTypeList;
            }
        }
        return new HashSet<>();
    }

    private static Set<String> set(String... strings) {
        return Arrays.stream(strings).collect(Collectors.toSet());
    }

}
