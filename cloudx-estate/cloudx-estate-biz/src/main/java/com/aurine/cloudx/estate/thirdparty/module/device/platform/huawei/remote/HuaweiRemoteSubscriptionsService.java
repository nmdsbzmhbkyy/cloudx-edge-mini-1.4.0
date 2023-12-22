package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.remote;

import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.config.HuaweiConfigDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.dto.HuaweiRespondDTO;
import com.aurine.cloudx.estate.thirdparty.remote.BaseRemote;

/**
 * @ClassName: HuaweiRemoteSubscriptionsService
 * @author: 王良俊 <>
 * @date: 2020年11月25日 下午02:50:51
 * @Copyright:
 */
public interface HuaweiRemoteSubscriptionsService extends BaseRemote {

    /**
     * <p>
     * 新增订阅
     * </p>
     *
     * @param callbackurl 回调地址
     * @param channel     协议通道
     * @param resource    订阅资源名
     * @param event       订阅资源事件
     */
    HuaweiRespondDTO addSubscriptions(HuaweiConfigDTO configDTO, String callbackurl, String channel, String resource, String event);

    /**
     * <p>
     * 修改订阅
     * </p>
     *
     * @param callbackurl    回调地址
     * @param channel        协议通道
     * @param subscriptionId 订阅ID
     */
    HuaweiRespondDTO updateSubscriptions(HuaweiConfigDTO configDTO, String subscriptionId, String callbackurl, String channel);

    /**
     * <p>
     * 删除订阅
     * </p>
     *
     * @param subscriptionId 订阅ID
     */
    HuaweiRespondDTO delSubscriptions(HuaweiConfigDTO configDTO, String subscriptionId);


}
