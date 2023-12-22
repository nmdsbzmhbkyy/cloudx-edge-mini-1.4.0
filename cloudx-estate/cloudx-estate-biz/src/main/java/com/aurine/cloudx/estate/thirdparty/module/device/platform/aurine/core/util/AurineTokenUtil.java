package com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.core.util;

import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.constant.AurineCacheConstant;
import com.aurine.cloudx.estate.util.RedisUtil;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-03
 * @Copyright:
 */
public class AurineTokenUtil {

    public static String getToken(int projectId, boolean isProject) {
        String traStr = "";

        if (isProject) {
            traStr = "_" + projectId;
        }

        if (RedisUtil.hasKey(AurineCacheConstant.AURINE_TOKEN + traStr)) {
            Object obj = RedisUtil.get(AurineCacheConstant.AURINE_TOKEN + traStr);
            if (obj == null) {
                return null;
            }
            return String.valueOf(obj);
        } else {
            return null;
        }
    }

    public static void setToken(String token, int projectId, boolean isProject) {
        String traStr = "";

        if (isProject) {
            traStr = "_" + projectId;
        }

        RedisUtil.set(AurineCacheConstant.AURINE_TOKEN + traStr, token, AurineCacheConstant.AURINE_TOKEN_TTL);
    }

    public static void removeToken(int projectId, boolean isProject) {
        String traStr = "";

        if (isProject) {
            traStr = "_" + projectId;
        }
        RedisUtil.del(AurineCacheConstant.AURINE_TOKEN + traStr);
    }

    /**
     * 是否存在登陆锁
     *
     * @return
     */
    public static boolean hasLogin(int projectId, boolean isProject) {
        String traStr = "";

        if (isProject) {
            traStr = "_" + projectId;
        }
        return RedisUtil.hasKey(AurineCacheConstant.AURINE_LOGIN_LOCK + traStr);
    }

    /**
     * 上登陆锁
     *
     * @return
     */
    public static void loginLock(int projectId, boolean isProject) {
        String traStr = "";

        if (isProject) {
            traStr = "_" + projectId;
        }
        RedisUtil.set(AurineCacheConstant.AURINE_LOGIN_LOCK + traStr, "1", AurineCacheConstant.AURINE_LOGIN_LOCK_TTL);
    }

    /**
     * 解除登陆锁
     *
     * @return
     */
    public static void loginUnlock() {
        RedisUtil.del(AurineCacheConstant.AURINE_LOGIN_LOCK);
    }
}
