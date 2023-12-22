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
 * 井盖
 * </p>
 *
 * @author : 王良俊
 * @date : 2021-07-19 09:11:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManholeCoverShangrunEventData extends AbstractShangrunEventData {

    // 设备类型
    private int devType;

    // 设备编号
    private int devNo;

    // 数据类型
    private int dataType;

    // 信号强度
    private int rsrp;

    // 噪声强度
    private int snr;

    // 电池电量
    @JsonDeserialize(using = BatteryVoltageDeserializer.class)
    private int batteryVoltage;

    // 采集时间
    @JsonDeserialize(using = DateDeserializer.class)
    private LocalDate date;

    // 井盖状态 0：关闭，1：开盖
    private int openStatus;

    // 开锁状态 0：闭锁，1=蓝牙开锁器开锁， 2：远程授权开锁，3：激活开盖报警功能，4：禁用开盖报警功能
    private int lockStatus;

    // 设置报警倾角 允许范围=10-180 度（与水平面夹角）—注：倾斜报警角度缺省为 30°。
    private int dipAngleThreshold;

    // 实际倾角 精确到 1 度，0-180 度，与水平面夹角。
    private int dipAngle;


    // 唤醒次数 3D 唤醒次数（0-255）最大为 255，超过 255 只显示 255；每次报警后清零。
    private int wakeUpTimes;

    @Override
    protected IotEventCallback getEventByDevStatus(int code, String deviceId, LocalDateTime evenTime) {
        return new IotEventCallback(
                deviceId,
                "失联",
                evenTime
        );
    }

    @Override
    protected IotEventCallback getEventByAlarm(int code, String deviceId, LocalDateTime evenTime) {
        IotEventCallback iotEventCallback = null;
        switch (code) {
            case 0:
                iotEventCallback = new IotEventCallback(
                        deviceId,
                        "开盖报警",
                        evenTime
                );
                break;
            case 1:
                iotEventCallback = new IotEventCallback(
                        deviceId,
                        "超磁报警",
                        evenTime
                );
                break;
            case 2:
                iotEventCallback = new IotEventCallback(
                        deviceId,
                        "低压报警标志",
                        "电量" + batteryVoltage + "%",
                        evenTime
                );
                break;
            case 3:
                iotEventCallback = new IotEventCallback(
                        deviceId,
                        "强烈撞击报警",
                        evenTime
                );
                break;
            case 4:
                iotEventCallback = new IotEventCallback(
                        deviceId,
                        "盗窃报警",
                        evenTime
                );
                break;
            case 5:
                iotEventCallback = new IotEventCallback(
                        deviceId,
                        "水位报警",
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
        return ThirdPartyDeviceTypeEnum.MANHOLE_COVER.devType;
    }

    @Override
    public String toString() {
        return "ManholeCoverEventData{" +
                "devType=" + devType +
                ", devNo=" + devNo +
                ", dataType=" + dataType +
                ", rsrp=" + rsrp +
                ", snr=" + snr +
                ", batteryVoltage=" + batteryVoltage +
                ", date=" + date +
                ", openStatus=" + openStatus +
                ", lockStatus=" + lockStatus +
                ", dipAngleThreshold=" + dipAngleThreshold +
                ", dipAngle=" + dipAngle +
                ", wakeUpTimes=" + wakeUpTimes +
                '}';
    }

}
