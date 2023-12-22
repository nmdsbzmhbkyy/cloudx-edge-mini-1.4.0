package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.core.util;

import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.constant.HuaweiCacheConstant;
import com.aurine.cloudx.estate.util.RedisUtil;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-03
 * @Copyright:
 */
public class HuaweiTokenUtil {

    public static String getToken() {
        if (RedisUtil.hasKey(HuaweiCacheConstant.HUAWEI_TOKEN)) {
            Object obj = RedisUtil.get(HuaweiCacheConstant.HUAWEI_TOKEN);
            if (obj == null){
                return null;
            }
            return String.valueOf(obj);
        } else {
            return null;
        }
    }

    public static void setToken(String token) {
        RedisUtil.set(HuaweiCacheConstant.HUAWEI_TOKEN, token, HuaweiCacheConstant.HUAWEI_TOKEN_TTL);
    }

    public static void removeToken() {
        RedisUtil.del(HuaweiCacheConstant.HUAWEI_TOKEN);
    }


    /**
     * 是否存在登陆锁
     *
     * @return
     */
    public static boolean hasLogin() {
        return RedisUtil.hasKey(HuaweiCacheConstant.HUAWEI_LOGIN_LOCK);
    }

    /**
     * 上登陆锁
     *
     * @return
     */
    public static void loginLock() {
        RedisUtil.set(HuaweiCacheConstant.HUAWEI_LOGIN_LOCK, "1", HuaweiCacheConstant.HUAWEI_LOGIN_LOCK_TTL);
    }

    /**
     * 解除登陆锁
     *
     * @return
     */
    public static void loginUnlock() {
        RedisUtil.del(HuaweiCacheConstant.HUAWEI_LOGIN_LOCK);
    }
}
