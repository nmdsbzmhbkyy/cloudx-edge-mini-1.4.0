

package com.aurine.cloudx.estate.thirdparty.module.wr20.callback;


import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.thirdparty.module.wr20.service.BaseService;

/**
 * WR20 住户接口
 *
 * @ClassName: WR20FrameService
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-04 10:53
 * @Copyright:
 */
public interface WR20FrameCallbackService extends BaseService {


    /**
     * 同步框架，组团、楼栋、房屋、单元等
     *
     * @param jsonObject
     * @param gatewayId  网关ID
     * @return
     */
    void syncFrame(JSONObject jsonObject, String gatewayId);

}
