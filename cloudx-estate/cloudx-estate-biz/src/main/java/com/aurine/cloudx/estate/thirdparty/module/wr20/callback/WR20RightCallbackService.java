

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
public interface WR20RightCallbackService extends BaseService {

    /**
     * 变更通行凭证状态，如果原通行凭证不存在，则直接创建一条凭证记录
     *
     * @param jsonObject
     * @param gatewayId
     */
    void changeCertStateByWR20(JSONObject jsonObject, String gatewayId, String eventCode);

    /**
     * 新增人脸
     *
     * @param jsonObject
     * @return
     */
    void addFace(JSONObject jsonObject, String gatewayId, String uid);

    /**
     * 新增卡片
     *
     * @param jsonObject
     * @return
     */
    void addCard(JSONObject jsonObject, String gatewayId, String uid);

}
