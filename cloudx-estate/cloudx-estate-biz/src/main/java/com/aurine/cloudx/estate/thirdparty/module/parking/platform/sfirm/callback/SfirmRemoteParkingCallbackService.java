

package com.aurine.cloudx.estate.thirdparty.module.parking.platform.sfirm.callback;

import com.alibaba.fastjson.JSONObject;
import org.springframework.scheduling.annotation.Async;

/**
 * <p>赛菲姆 车场回调接口</p>
 * @ClassName: SfirmRemoteParkingCallbackService
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-07-29 14:16
 * @Copyright:
 */
public interface SfirmRemoteParkingCallbackService extends BaseService {


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
