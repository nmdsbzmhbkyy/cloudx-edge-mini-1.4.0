package com.aurine.cloudx.estate.util.delay.constants;

import lombok.AllArgsConstructor;

/**
 * @ClassName: 存放各种延时任务的话题
 * @author: 王良俊 <>
 * @date: 2020年11月03日 上午09:04:33
 * @Copyright:
 */
@AllArgsConstructor
public enum DelayTaskTopicEnum {
    /**
     * 住户过期处理
     */
    householderExp(DelayTaskTopicConstant.HOUSE_HOLDER_EXP),
    /**
     * 访客签离
     */
    visitorCheckOut(DelayTaskTopicConstant.VISITOR_CHECK_OUT),
    /**
     * 访客介质下发
     */
    visitorSendCert(DelayTaskTopicConstant.VISITOR_SEND_CERT),
    /**
     * 媒体广告下发
     */
//    mediaADSend(DelayTaskTopicConstant.MEDIA_AD_SEND),
    /**
     * 媒体广告移除
     */
//    mediaADRemove(DelayTaskTopicConstant.MEDIA_AD_REMOVE),

    ;

    public final String topic;

    public String getTopic() {
        return topic;
    }

    public static DelayTaskTopicEnum getEnumByTopic(String topic) {
        for (DelayTaskTopicEnum topicEnum : DelayTaskTopicEnum.values()) {
            if (topicEnum.getTopic().equals(topic)) {
                return topicEnum;
            }
        }
        return null;
    }
}
