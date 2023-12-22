package com.aurine.cloudx.estate.thirdparty.module.parking.factory;

import com.aurine.cloudx.estate.thirdparty.module.parking.platform.ParkingService;

/**
 * <p>车场抽象工厂</p>
 * @ClassName: AbstractParkingFactory
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-07-29 10:12
 * @Copyright:
 */
public abstract class AbstractParkingFactory {

    /**
     * 获取区口机接口实例
     * @return
     */
    public abstract ParkingService getParkingService();
}
