package com.aurine.cloudx.open.origin.constant.enums;

import lombok.AllArgsConstructor;

/**
 * <p>订单类型</p>
 * @ClassName: billingOrderTypeEnum
 * @author: 黄阳光 <huangyg@aurine.cn>
 * @date: 2020/7/10 9:05
 * @Copyright:
 */
@AllArgsConstructor
public enum BillingOrderTypeEnum {
    /**
     * 月租车充值
     */
    RECHARGE("1"),
    /**
     * 临时车缴费
     */
    PAY("2");

    public String code;
}
