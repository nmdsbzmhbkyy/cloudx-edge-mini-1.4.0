package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.alarm.entity.shangrun;

import com.aurine.cloudx.estate.entity.IotEventCallback;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.jackson.deserializer.DateDeserializer;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.enums.ThirdPartyDeviceTypeEnum;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 液位计
 * </p>
 *
 * @author : 王良俊
 * @date : 2021-07-19 08:57:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LevelGaugeShangrunEventData extends AbstractShangrunEventData {

    // 设备类型
    private int devType;

    // 设备编号
    private int devNo;

    // 数据类型 0：正常
    private int dataType;

    // 信号强度
    private int rsrp;

    // 噪声强度
    private int snr;

    // 电池电量
    private int batteryVoltage;

    // 采集时间
    @JsonDeserialize(using = DateDeserializer.class)
    private LocalDate date;

    // 液位值
    private float liquidValue;

    // 温度
    private float temperature;

    // 液位阀值
    private float waterLevelThreshold;

    @Override
    protected IotEventCallback getEventByDevStatus(int code, String deviceId, LocalDateTime evenTime) {
        IotEventCallback iotEventCallback = null;
        switch (code) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
                break;
            case 5:
                iotEventCallback = new IotEventCallback(
                        deviceId,
                        "未入低功耗模式",
                        evenTime
                );
                break;
            case 6:
                iotEventCallback = new IotEventCallback(
                        deviceId,
                        "欠压报警",
                        "电量：" + batteryVoltage + "%",
                        evenTime
                );
                break;
            case 7:
                iotEventCallback = new IotEventCallback(
                        deviceId,
                        "传感器故障",
                        evenTime
                );
                break;
            default:
        }
        return iotEventCallback;
    }

    @Override
    protected IotEventCallback getEventByAlarm(int code, String deviceId, LocalDateTime evenTime) {
        IotEventCallback iotEventCallback = null;
        switch (code) {
            case 0:
                iotEventCallback = new IotEventCallback(
                        deviceId,
                        "超磁报警",
                        evenTime
                );
                break;
            case 1:
                break;
            case 2:
                iotEventCallback = new IotEventCallback(
                        deviceId,
                        "液位超下限报警",
                        "液位深度：" + liquidValue,
                        evenTime
                );
                break;
            case 3:
                iotEventCallback = new IotEventCallback(
                        deviceId,
                        "液位超上限报警",
                        "液位深度：" + liquidValue,
                        evenTime
                );
                break;
            case 4:
            case 5:
            case 6:
            case 7:
            default:
        }
        return iotEventCallback;

    }

    @Override
    public String getDeviceType() {
        return ThirdPartyDeviceTypeEnum.LEVEL_GAUGE.devType;
    }

    @Override
    public String toString() {
        return "LevelGaugeEventData{" +
                "devType=" + devType +
                ", devNo=" + devNo +
                ", dataType=" + dataType +
                ", rsrp=" + rsrp +
                ", snr=" + snr +
                ", batteryVoltage=" + batteryVoltage +
                ", date=" + date +
                ", liquidValue=" + liquidValue +
                ", temperature=" + temperature +
                ", waterLevelThreshold=" + waterLevelThreshold +
                '}';
    }
}
