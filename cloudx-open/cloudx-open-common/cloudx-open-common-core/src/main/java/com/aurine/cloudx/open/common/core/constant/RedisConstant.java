package com.aurine.cloudx.open.common.core.constant;

import org.apache.commons.lang3.StringUtils;

/**
 * Redis常量
 *
 * @author : Qiu
 * @date : 2022 01 24 10:35
 */
public class RedisConstant {

    /**
     * RedisKey值分隔符
     */
    private static final String REDIS_KEY_SEPARATOR = "_";

    /**
     * Redis数据库存放的key的前缀
     */
    private static final String REDIS_KEY_PREFIX = "cloudx-open:open";

    /**
     * 获取Redis原数据的key
     *
     * @param suffix key的后缀
     * @return 完整的key字符串
     */
    public static String getRedisKey(String suffix) {
        if (StringUtils.isEmpty(suffix)) suffix = "";
        return REDIS_KEY_PREFIX + REDIS_KEY_SEPARATOR + suffix;
    }
}
