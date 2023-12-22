package com.aurine.cloudx.estate.thirdparty.module.device.factory;

import com.aurine.cloudx.estate.thirdparty.module.device.platform.DeviceService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.IotDeviceService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.PassWayDeviceService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.PerimeterAlarmDeviceService;

/**
 * 设备抽象工厂
 *
 * @ClassName: AbstractDeviceFactory
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-07-31 8:51
 * @Copyright:
 */
public abstract class AbstractDeviceFactory {

    /**
     * 获取通行设备接口实例
     *
     * @return
     */
    public abstract PassWayDeviceService getPassWayDeviceService();

    /**
     * 获取周界告警设备接口实例
     *
     * @return
     */
    public abstract PerimeterAlarmDeviceService getPerimeterAlarmDeviceService();

    /**
     * 获取泛感设备接口实例
     *
     * @return
     */
    public abstract IotDeviceService getIotDeviceService();

    /**
     * 获取设备接口实例
     * @return
     */
    public abstract DeviceService getDeviceService();


}
