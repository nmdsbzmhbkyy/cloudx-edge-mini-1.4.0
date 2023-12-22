package com.aurine.cloudx.estate.thirdparty.module.parking.platform.reformer.callback;

import com.alibaba.fastjson.JSONObject;
import org.springframework.scheduling.annotation.Async;

/**
 * @version 1.0
 * @author： 林功鑫
 * @date： 2021-06-09 10:39
 */
public interface ReformerRemoteParkingCallbackService extends BaseService{
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
}
