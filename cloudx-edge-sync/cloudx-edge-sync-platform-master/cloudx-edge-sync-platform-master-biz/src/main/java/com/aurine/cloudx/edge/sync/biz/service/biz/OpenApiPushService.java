package com.aurine.cloudx.edge.sync.biz.service.biz;

import com.aurine.cloudx.edge.sync.common.entity.OpenApiEntity;

/**
 * @Author: wrm
 * @Date: 2022/01/20 8:46
 * @Package: com.aurine.cloudx.edge.sync.biz.service
 * @Version: 1.0
 * @Remarks:
 **/
public interface OpenApiPushService {

    /**
     * 处理 config 订阅消息
     * @param requestObj
     * @return 成功/失败
     */
    Boolean dealConfigRequest(OpenApiEntity requestObj);

    /**
     * 处理open平台回调事件消息
     * @param requestObj
     * @return 成功/失败
     */
    Boolean dealEventRequest(OpenApiEntity requestObj);

    /**
     * 处理open平台回调操作消息
     * @param requestObj
     * @return 成功/失败
     */
    Boolean dealOperateRequest(OpenApiEntity requestObj);

    /**
     * 处理open平台回调命令消息
     * @param requestObj
     * @return 成功/失败
     */
    Boolean dealCommandRequest(OpenApiEntity requestObj);

    /**
     * 处理open平台回调级联消息
     * @param requestObj
     * @return 成功/失败
     */
    Boolean dealCascadeRequest(OpenApiEntity requestObj);

    /**
     * 处理open平台回调其他消息
     * @param requestObj
     * @return 成功/失败
     */
    Boolean dealOtherRequest(OpenApiEntity requestObj);
}
