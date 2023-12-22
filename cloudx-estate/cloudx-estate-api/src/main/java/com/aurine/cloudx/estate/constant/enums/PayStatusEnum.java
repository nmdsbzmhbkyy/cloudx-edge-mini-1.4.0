package com.aurine.cloudx.estate.constant.enums;

import lombok.AllArgsConstructor;

/**
 * 缴费状态
 * @ClassName: PayStatusEnum
 * @author: xull@aurine.cn
 * @date: 2020/7/10 11:05
 * @Copyright:
 */
@AllArgsConstructor
public enum PayStatusEnum {
    /**
     * 未缴
     */
    UNPAID("1"),
    /**
     * 已缴
     */
    PAID("2"),
    /**
     * 已退款
     */
    REFUND("3"),
    /**
     * 预存
     */
    PREPAID("4")
    ;
    public String code;
}
