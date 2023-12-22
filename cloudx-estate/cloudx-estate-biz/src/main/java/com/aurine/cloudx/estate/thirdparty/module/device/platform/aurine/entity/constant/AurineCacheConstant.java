package com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.constant;

/**
 * 缓存用常量
 *
 * @ClassName: CacheConstant
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-11 16:01
 * @Copyright:
 */
public class AurineCacheConstant {
    /**
     * Aurine中台 登录锁
     */
    public static final String AURINE_LOGIN_LOCK = "AURINE_MIDDLE_LOGIN_LOCK";
    /**
     * Aurine中台 Token
     */
    public static final String AURINE_TOKEN = "AURINE_MIDDLE_TOKEN";

    /**
     * 冠林中台 下发凭证Redis缓存前缀
     */
    public static final String AURINE_CERT_COMMAND_PER = "AURINE_MIDDLE:CERT:";

    /**
     * 冠林中台 下发到设备列表
     */
    public static final String AURINE_CERT_DEVICE_SET = "AURINE_MIDDLE:CERT_MANAGE:DEVICE_SET";

    /**
     * 冠林中台  凭证处理完成列表
     */
    public static final String AURINE_CERT_DONE_LIST = "AURINE_MIDDLE:CERT_MANAGE:DONE_LIST";
    /**
     * 冠林中台 处理完成凭证值 前缀 + SN + ":"+mshId
     */
    public static final String AURINE_CERT_DONE_STR_PER = "AURINE_MIDDLE_DONE:";

    /**
     * 冠林中台 消息锁前缀
     */
    public static final String AURINE_CERT_LOCK_PER = "AURINE_MIDDLE:CERT_MANAGE:LOCK:";

    /**
     * 冠林中台 下发凭证Redis缓存队列超时时间
     */
    public static final long AURINE_CERT_COMMAND_TTL = 300;// 5min


    /**
     * 冠林中台 下发凭证 启动锁
     */
    public static final String AURINE_CERT_COMMAND_LOCK_PER = "AURINE_MIDDLE:CERT_LOCK:";

    /**
     * Aurine中台 登录锁持续时间 秒
     */
    public static final long AURINE_LOGIN_LOCK_TTL = 5;

    public static final long AURINE_MESSAGE_CACHE_TTL = 30;
    /**
     * Aurine中台 Token持续时间
     */
    public static final long AURINE_TOKEN_TTL = (60 * 24 * 7) - 100;
}
