

package com.aurine.cloudx.estate.thirdparty.module.parking.platform.fujica.callback;

import com.alibaba.fastjson.JSONObject;
import org.springframework.scheduling.annotation.Async;

import java.io.IOException;

/**
 * <p>富士 车场回调接口</p>
 * @ClassName: SfirmRemoteParkingCallbackService
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-05-21 13:53
 * @Copyright:
 */
public interface FujicaRemoteParkingCallbackService extends BaseService {


    /**
     * 回调添加车辆入场纪录
     *
     * @param jsonObject
     * @return
     */
    @Async
    void enterCar(JSONObject jsonObject);

    /**
     * 回调添加车辆出场纪录
     *
     * @param jsonObject
     * @return
     */
    @Async
    void outerCar(JSONObject jsonObject);

    /**
     * 回调添加车辆入场图片
     *
     * @param jsonObject
     * @return
     */
    @Async
    void enterImg(JSONObject jsonObject);

    /**
     * 回调添加车辆出场图片
     *
     * @param jsonObject
     * @return
     */
    @Async
    void outerImg(JSONObject jsonObject);


    /**
     * 设置车场上下线状态
     *
     * @param jsonObject
     */
    @Async
    void isOnline(JSONObject jsonObject);

}
