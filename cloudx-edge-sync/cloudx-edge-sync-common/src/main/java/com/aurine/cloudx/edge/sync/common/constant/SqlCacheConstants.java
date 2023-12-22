package com.aurine.cloudx.edge.sync.common.constant;

/**
 * @Author: wrm
 * @Date: 2022/11/04 14:45
 * @Package: com.aurine.cloudx.edge.sync.common.constant
 * @Version: 1.0
 * @Remarks:
 **/
public class SqlCacheConstants {
    /**
     * 刷新缓存频率
     */
    public static final Integer FLUSH_RATE = 1;

    /**
     * SQL缓存
     */
    public static final String SQL_CACHE_KEY = "SQL_CACHE_KEY";
    /**
     *
     */
    public static final String SQL_CACHE_KEY_LOCK = "SQL_CACHE_KEY_LOCK";
    public static final Long SQL_CACHE_KEY_LOCK_TTL = 3L;

}
