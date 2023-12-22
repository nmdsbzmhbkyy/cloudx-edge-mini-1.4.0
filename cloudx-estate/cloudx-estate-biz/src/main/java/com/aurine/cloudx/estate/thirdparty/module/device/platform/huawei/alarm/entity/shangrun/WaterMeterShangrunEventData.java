package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.alarm.entity.shangrun;

import com.aurine.cloudx.estate.entity.IotEventCallback;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.jackson.deserializer.BatteryVoltageDeserializer;
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
 * 水表
 * </p>
 *
 * @author : 王良俊
 * @date : 2021-07-19 08:59:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WaterMeterShangrunEventData extends AbstractShangrunEventData {


    // 设备类型

    private int devType;

    // 数据类型（暂时没用，也不知道有什么用）
    private int dataType;

    // 信号强度
    private int rsrp;

    // 噪声强度
    private int snr;

    // 电池电量（电量百分比 3.0V 为 0%，3.66V 为 100%）
    @JsonDeserialize(using = BatteryVoltageDeserializer.class)
    private int batteryVoltage;

    // 采集时间
    @JsonDeserialize(using = DateDeserializer.class)
    private LocalDate date;

    // 总流量
    private int totalFlow;

    @Override
    protected IotEventCallback getEventByDevStatus(int code, String deviceId, LocalDateTime evenTime) {
        IotEventCallback iotEventCallback = null;
        switch (code) {
            case 0:
            case 1:
            case 2:
            case 3:
                break;
            case 4:
                iotEventCallback = new IotEventCallback(
                        deviceId,
                        "反转不计流量状态",
                        evenTime
                );
                break;
            case 5:
                iotEventCallback = new IotEventCallback(
                        deviceId,
                        "不能进入低功耗模式",
                        evenTime
                );
                break;
            case 6:
                iotEventCallback = new IotEventCallback(
                        deviceId,
                        "计量欠压（内电池）",
                        "电量" + batteryVoltage + "%",
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
                iotEventCallback = new IotEventCallback(
                        deviceId,
                        "开盖报警",
                        evenTime
                );
                break;
            case 2:
                iotEventCallback = new IotEventCallback(
                        deviceId,
                        "漏流报警",
                        evenTime
                );
                break;
            case 3:
                iotEventCallback = new IotEventCallback(
                        deviceId,
                        "过流报警",
                        evenTime
                );
                break;
            case 4:
                iotEventCallback = new IotEventCallback(
                        deviceId,
                        "模组欠压（低于 3.2V）",
                        evenTime
                );
                break;
            case 5:
                iotEventCallback = new IotEventCallback(
                        deviceId,
                        "发生 FOTA 升级",
                        evenTime
                );
                break;
            case 6:
            case 7:
                break;
            default:
        }
        return iotEventCallback;
    }

    @Override
    public String getDeviceType() {
        return ThirdPartyDeviceTypeEnum.WATER_METER.devType;
    }

    @Override
    public String toString() {
        return "WaterMeterEventData{" +
                "devType=" + devType +
                ", dataType=" + dataType +
                ", rsrp=" + rsrp +
                ", snr=" + snr +
                ", batteryVoltage=" + batteryVoltage +
                ", date=" + date +
                ", totalFlow=" + totalFlow +
                '}';
    }

}
