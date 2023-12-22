package com.aurine.cloudx.estate.thirdparty.remote.edge.ali.core.entity;

/**
 * 缓存用常量
 *
 * @ClassName: CacheConstant
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-06-08 15:45
 * @Copyright:
 */
public class AliEdgeCacheConstant {
    /**
     * 登录锁
     */
    public static final String ALI_EDGE_LOGIN_LOCK = "ALI_EDGE_LOGIN_LOCK";
    /**
     *  Token
     */
    public static final String ALI_EDGE_TOKEN = "ALI_EDGE_TOKEN";

    /**
     *  登录锁持续时间 秒
     */
    public static final long ALI_EDGE_LOGIN_LOCK_TTL = 15;

    /**
     *  Token持续时间
     */
    public static final long ALI_EDGE_TOKEN_TTL = (60 * 24 * 7) - 10;

    /**
     * 中台请求前缀
     */
    public static final String ALI_EDGE_REQUEST_PER = "ALI_EDGE_MSG_";

    /**
     * 请求等待超时时间
     */
    public static final long ALI_EDGE_REQUEST_MSG_TTL = 60 * 2;
}
