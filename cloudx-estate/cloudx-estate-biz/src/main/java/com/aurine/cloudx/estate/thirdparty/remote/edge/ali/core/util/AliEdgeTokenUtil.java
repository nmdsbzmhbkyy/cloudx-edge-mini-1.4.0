package com.aurine.cloudx.estate.thirdparty.remote.edge.ali.core.util;

import com.aurine.cloudx.estate.thirdparty.remote.edge.ali.core.entity.AliEdgeCacheConstant;
import com.aurine.cloudx.estate.util.RedisUtil;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-03
 * @Copyright:
 */
public class AliEdgeTokenUtil {

    public static String getToken() {
        if (RedisUtil.hasKey(AliEdgeCacheConstant.ALI_EDGE_TOKEN)) {
            Object obj = RedisUtil.get(AliEdgeCacheConstant.ALI_EDGE_TOKEN);
            if (obj == null) {
                return null;
            }
            return String.valueOf(obj);
        } else {
            return null;
        }
    }

    public static void setToken(String token) {
        RedisUtil.set(AliEdgeCacheConstant.ALI_EDGE_TOKEN, token, AliEdgeCacheConstant.ALI_EDGE_TOKEN_TTL);
    }

    public static void removeToken() {
        RedisUtil.del(AliEdgeCacheConstant.ALI_EDGE_TOKEN);
    }


    /**
     * 是否存在登陆锁
     *
     * @return
     */
    public static boolean hasLogin() {
        return RedisUtil.hasKey(AliEdgeCacheConstant.ALI_EDGE_LOGIN_LOCK);
    }

    /**
     * 上登陆锁
     *
     * @return
     */
    public static void loginLock() {
        RedisUtil.set(AliEdgeCacheConstant.ALI_EDGE_LOGIN_LOCK, "1", AliEdgeCacheConstant.ALI_EDGE_LOGIN_LOCK_TTL);
    }

    /**
     * 解除登陆锁
     *
     * @return
     */
    public static void loginUnlock() {
        RedisUtil.del(AliEdgeCacheConstant.ALI_EDGE_LOGIN_LOCK);
    }
}
