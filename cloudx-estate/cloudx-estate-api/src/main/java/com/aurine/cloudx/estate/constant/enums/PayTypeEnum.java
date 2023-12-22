package com.aurine.cloudx.estate.constant.enums;

import lombok.AllArgsConstructor;

/**
 * <p>支付方式</p>
 * @ClassName: PayTypeEnum
 * @author: 黄阳光 <huangyg@aurine.cn>
 * @date: 2020/7/10 11:05
 * @Copyright:
 */
@AllArgsConstructor
public enum PayTypeEnum {
    /**
     * 现金/转账
     */
    CASH("1"),
    /**
     * 线下微信
     */
    OFFLINE_WECHAT("2"),
    /**
     * 线下支付宝
     */
    OFFLINE_ALIPAY("3"),
    /**
     * 微信
     */
    WECHAT("4"),
    /**
     * 支付宝
     */
    ALIPAY("5"),
    /**
     * 其他
     */


    /**
     * 小程序物业
     */
    MINIAPPWY("7"),
    /**
     * 小程序业主
     */
    MINIAPPYZ("8"),
    /**
     * APP物业
     */
    APPWY("9"),

    /**
     * app业主
     */
    APPYZ("10"),


    OTHER("6");
    public String code;
}
