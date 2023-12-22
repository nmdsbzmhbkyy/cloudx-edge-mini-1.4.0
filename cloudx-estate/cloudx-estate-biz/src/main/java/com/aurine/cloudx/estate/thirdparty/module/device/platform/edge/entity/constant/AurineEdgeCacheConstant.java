package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant;

/**
 * 缓存用常量
 *
 * @ClassName: CacheConstant
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-03 10:01
 * @Copyright:
 */
public class AurineEdgeCacheConstant {
    /**
     * 华为中台 登录锁
     */
    public static final String AURINE_EDGE_LOGIN_LOCK = "AURINE_EDGE_MIDDLE_LOGIN_LOCK";
    /**
     * 华为中台 Token
     */
    public static final String AURINE_EDGE_TOKEN = "AURINE_EDGE_MIDDLE_TOKEN";

    /**
     * 华为中台 登录锁持续时间 秒
     */
    public static final long AURINE_EDGE_LOGIN_LOCK_TTL = 15;

    /**
     * 华为中台 Token持续时间
     */
    public static final long AURINE_EDGE_TOKEN_TTL = (60 * 24 * 7) - 10;

    /**
     * 华为中台请求前缀
     */
    public static final String AURINE_EDGE_REQUEST_PER = "AURINE_EDGE_MIDDLE_MSG_";

    /**
     * 华为中台 请求等待超时时间
     */
    public static final long AURINE_EDGE_REQUEST_MSG_TTL = 60 * 2;
}
