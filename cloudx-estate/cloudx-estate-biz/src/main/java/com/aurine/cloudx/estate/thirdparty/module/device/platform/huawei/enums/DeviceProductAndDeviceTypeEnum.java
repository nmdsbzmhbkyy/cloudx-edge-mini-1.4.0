package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.enums;

import com.aurine.cloudx.common.core.constant.DeviceTypeConstants;
import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 设备产品和设备类型枚举类
 * 根据 http://139.9.139.196:4999/web/#/12?page_id=491维护
 * </p>
 * @author : 王良俊
 * @date : 2021-07-22 18:22:12
 */
@AllArgsConstructor
public enum DeviceProductAndDeviceTypeEnum {

    GL8_K3CS("3T11K3C01", set(DeviceTypeConstants.LADDER_WAY_DEVICE, DeviceTypeConstants.GATE_DEVICE)),
    GL8_K4CS("3T11K4C01", set(DeviceTypeConstants.LADDER_WAY_DEVICE, DeviceTypeConstants.GATE_DEVICE)),
    GL8_T3CS("3T11T3C01", set(DeviceTypeConstants.LADDER_WAY_DEVICE, DeviceTypeConstants.GATE_DEVICE)),
    GL8_K6CS("3T11K6C01", set(DeviceTypeConstants.LADDER_WAY_DEVICE, DeviceTypeConstants.GATE_DEVICE)),
    GL8_K3S("3T30K3S01", set(DeviceTypeConstants.LADDER_WAY_DEVICE, DeviceTypeConstants.GATE_DEVICE)),
    GL8_K4S("3T30K4S01", set(DeviceTypeConstants.LADDER_WAY_DEVICE, DeviceTypeConstants.GATE_DEVICE)),
    GL8_K6S("3T30K6S01", set(DeviceTypeConstants.LADDER_WAY_DEVICE, DeviceTypeConstants.GATE_DEVICE)),
    GL8_T3S("3T30T3S01", set(DeviceTypeConstants.LADDER_WAY_DEVICE, DeviceTypeConstants.GATE_DEVICE)),
    GL8_M81S("3T30M81S01", set(DeviceTypeConstants.LADDER_WAY_DEVICE, DeviceTypeConstants.GATE_DEVICE)),
    GL8_UR2S("3T30UR2S01", set(DeviceTypeConstants.LADDER_WAY_DEVICE, DeviceTypeConstants.GATE_DEVICE)),
    GL8_M3("3T31M301", set(DeviceTypeConstants.LADDER_WAY_DEVICE, DeviceTypeConstants.GATE_DEVICE)),
    C2PP73G3ZJ("C2PP73G3ZJ", set(DeviceTypeConstants.LADDER_WAY_DEVICE, DeviceTypeConstants.GATE_DEVICE)),
    D3T00FFHA("3T00FFHA", set(DeviceTypeConstants.LADDER_WAY_DEVICE, DeviceTypeConstants.GATE_DEVICE)),

    // 待定，具体需要由硬件那边定义
    // GL8_K3CS("3T11K3C02", getList(DeviceTypeConstants.LADDER_WAY_DEVICE, DeviceTypeConstants.GATE_DEVICE)),

    // 系统中还没有的设备类型
    // Shangrun_roadAreaWaterMeter("F098SRA02", getList()),

    // IOT设备
    MANHOLE_COVER("ManholeCover", set(DeviceTypeConstants.SMART_MANHOLE_COVER)),
    SHANGRUN_MANHOLE_COVER("F098SRA05", set(DeviceTypeConstants.SMART_MANHOLE_COVER)),

    LIQUIDOMETER("Liquidometer", set(DeviceTypeConstants.LEVEL_GAUGE)),
    SHANGRUN_LIQUIDOMETER("F098SRA02", set(DeviceTypeConstants.LEVEL_GAUGE)),

    QIYU_SMART_STREET_LIGHT("Z298QYA01", set(DeviceTypeConstants.SMART_STREET_LIGHT)),

    SMOKE("SmokeDetector", set(DeviceTypeConstants.SMOKE)),
    QIYU_SMOKE("Z398QYA01", set(DeviceTypeConstants.SMOKE)),

    SHANGRUN_WATER_METER("Z198SRA20", set(DeviceTypeConstants.SMART_WATER_METER)),
    WATER_METER("WaterMeter", set(DeviceTypeConstants.SMART_WATER_METER));

    public String modelId;
    public Set<String> deviceTypeList;

    public static Set<String> getDeviceTypeListByModelId(String modelId) {
        for (DeviceProductAndDeviceTypeEnum value : DeviceProductAndDeviceTypeEnum.values()) {
            if (value.modelId.equals(modelId)) {
                return value.deviceTypeList;
            }
        }
        return null;
    }

    private static Set<String> set(String... strings) {
        return Arrays.stream(strings).collect(Collectors.toSet());
    }

}
