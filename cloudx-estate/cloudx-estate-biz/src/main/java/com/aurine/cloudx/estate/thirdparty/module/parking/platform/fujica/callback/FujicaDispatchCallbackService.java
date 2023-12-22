package com.aurine.cloudx.estate.thirdparty.module.parking.platform.fujica.callback;

import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.parking.platform.fujica.callback.factory.FujicaParkingCallbackFactory;
import com.aurine.project.entity.Event;
import org.springframework.stereotype.Service;

/**
 * @description: 富士车场回调分发接口
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-05-21
 * @Copyright:
 */
@Service
public class FujicaDispatchCallbackService {

    public void dispatch(Event event) {
        switch (event.getMethod()) {
            case "enterParking":
                FujicaParkingCallbackFactory.getInstance(VersionEnum.getByCode(event.getVersion())).enterCar(event.getBody());
                break;
            case "exitParking":
                FujicaParkingCallbackFactory.getInstance(VersionEnum.getByCode(event.getVersion())).outerCar(event.getBody());
                break;
            default:
                break;

        }
    }

}
