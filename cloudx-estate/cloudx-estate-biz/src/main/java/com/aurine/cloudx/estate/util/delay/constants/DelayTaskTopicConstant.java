package com.aurine.cloudx.estate.util.delay.constants;

import lombok.AllArgsConstructor;

/**
 * <p>
 *
 * </p>
 *
 * @ClassName: DelayTaskTopicConstant
 * @author: 王良俊 <>
 * @date: 2021年02月18日 上午10:51:00
 * @Copyright:
 */
public class DelayTaskTopicConstant {

    /**
     * 住户过期kafka主题
     */
    public static final String HOUSE_HOLDER_EXP = "DELAY_TASK_EXP_HOUSE_HOLDER";

    /**
     * 访客自动签离kafka主题
     */
    public static final String VISITOR_CHECK_OUT = "DELAY_TASK_CHECK_OUT_VISITORS";

    /**
     * 访客自动下发介质kafka主题
     */
    public static final String VISITOR_SEND_CERT = "DELAY_TASK_SEND_VISITOR_CERT";

/*    *//**
     * 广告发送接口
     *//*
    public static final String MEDIA_AD_SEND = "DELAY_TASK_MEDIA_AD_SEND";

    *//**
     * 广告删除接口
     *//*
    public static final String MEDIA_AD_REMOVE = "DELAY_TASK_MEDIA_AD_REMOVE";*/
}
