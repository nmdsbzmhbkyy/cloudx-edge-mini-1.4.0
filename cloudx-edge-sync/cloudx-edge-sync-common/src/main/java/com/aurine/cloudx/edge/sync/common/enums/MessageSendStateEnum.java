package com.aurine.cloudx.edge.sync.common.enums;

import lombok.AllArgsConstructor;

/**
 * @Author: wrm
 * @Date: 2022/01/20 18:17
 * @Package: com.aurine.cloudx.edge.sync.enums
 * @Version: 1.0
 * @Remarks:
 **/
@AllArgsConstructor
public enum MessageSendStateEnum {
    /**
     * 待发送
     */
    WAIT("0"),

    /**
     * 已发送未返回结果
     */
    SENDING("1"),

    /**
     * 已完成
     */
    COMPLETE("2"),

    /**
     * 跳过
     */
    SKIP("7"),

    /**
     * 异常
     */
    ERROR("8"),

    /**
     * 已过期
     */
    EXPIRED("9");

    public String code;
}
