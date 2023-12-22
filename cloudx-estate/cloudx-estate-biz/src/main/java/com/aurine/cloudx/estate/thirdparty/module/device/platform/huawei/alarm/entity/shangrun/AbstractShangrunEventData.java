package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.alarm.entity.shangrun;

import com.aurine.cloudx.estate.entity.IotEventCallback;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * Iot设备事件抽象类
 * </p>
 * @author : 王良俊
 * @date : 2021-07-19 14:40:43
 */
@Data
public abstract class AbstractShangrunEventData implements Serializable {


    // 设备状态
    protected int devStatus;

    // 报警状态
    protected int alarm;

    /**
    * <p>
    * 判断二进制对应bit位是否为1
    * </p>
    *
    * @param num 要进行判断的十进制数
    * @param position 要判断的位置
    * @return 如果为 1 则返回true
    */
    protected boolean checkBitIsOne(int num, int position) {
        return ((num >> position) & 1) == 1;
    }

    /**
     * <p>
     * 获取到本次回调的所有事件对象的列表
     * </p>
     *
     * @param deviceId 设备ID（非第三方设备ID）
     * @param evenTime 事件时间
     * @return 事件对象列表
     */
    public Set<IotEventCallback> getStatusAndAlarmEvents(String deviceId, LocalDateTime evenTime) {
        Set<IotEventCallback> iotEventCallbackList = new HashSet<>();
        if (devStatus != 0 || alarm != 0) {
            for (int i = 0; i < 8; i++) {
                if (this.checkBitIsOne(devStatus, i)) {
                    IotEventCallback eventByDevStatus = getEventByDevStatus(i, deviceId, evenTime);
                    if (eventByDevStatus != null) {
                        iotEventCallbackList.add(eventByDevStatus);
                    }
                }
                if (this.checkBitIsOne(alarm, i)) {
                    IotEventCallback eventByDevStatus = getEventByAlarm(i, deviceId, evenTime);
                    if (eventByDevStatus != null) {
                        iotEventCallbackList.add(eventByDevStatus);
                    }
                }
            }
        }
        return iotEventCallbackList;
    }

    /**
     * <p>
     * 设备状态处理
     * 根据传入的获取到事件对象
     * </p>
     *
     * @param code 为1的bit位数
     * @return 物联网设备事件对象
     */
    protected abstract IotEventCallback getEventByDevStatus(int code, String deviceId, LocalDateTime evenTime);

    /**
     * <p>
     * 报警状态处理
     * 根据传入的code获取到事件对象
     * </p>
     *
     * @param code 为1的bit位
     * @return 物联网设备事件对象
     */
    protected abstract IotEventCallback getEventByAlarm(int code, String deviceId, LocalDateTime evenTime);

    /**
     * <p>
     * 第三方物联网设备的设备类型ID（不是我们系统的）
     * </p>
     *
     * @return 设备类型ID（第三方）
     */
    public abstract String getDeviceType();

}
