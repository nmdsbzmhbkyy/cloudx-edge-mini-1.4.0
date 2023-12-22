

package com.aurine.cloudx.estate.thirdparty.module.wr20.callback;


import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.thirdparty.module.wr20.service.BaseService;

/**
 * WR20 设备回调接口
 *
 * @ClassName: WR20FrameService
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-12-17 14:04
 * @Copyright:
 */
public interface WR20DeviceCallbackService extends BaseService {


    /**
     * 同步设备
     *
     * @param jsonObject
     * @param gatewayId
     * @return
     */
    void syncDevice(JSONObject jsonObject, String deviceType, String gatewayId);

}
