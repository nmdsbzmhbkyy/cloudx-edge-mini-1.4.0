

package com.aurine.cloudx.estate.thirdparty.remote.edge.ali.callback.service;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.thirdparty.module.parking.platform.sfirm.callback.BaseService;
import org.springframework.scheduling.annotation.Async;

/**
 * @ClassName: AliEdgeCallbackParkingService
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-06-22 17:09
 * @Copyright:
 */
public interface AliEdgeCallbackParkingService {


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
     * 分发函数
     * @param data
     * @return
     */
    JSONObject dispatch(JSONObject data);

}
