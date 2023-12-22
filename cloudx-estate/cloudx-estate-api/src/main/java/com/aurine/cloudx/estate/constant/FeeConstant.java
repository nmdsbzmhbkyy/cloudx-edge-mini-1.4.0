package com.aurine.cloudx.estate.constant;

/**
 * Title: FeeConstant
 * Description: 费用相关常量
 *
 * @author xull@aurine.cn
 * @version 1.0.0
 * @date 2020/5/25 13:09
 */
public interface FeeConstant {
    /**
     * 收费周期：固定费用
     */
    String FIXED_CHARGE = "1";
    /**
     * 收费周期：临时费用
     */
    String INCIDENTAL_EXPENSES = "2";


    /**
     * 计费周期：提前一个周期收费
     */
    String CYCLE_AHEAD = "1";
    /**
     * 计费周期：收取当前周期费用
     */
    String CURRENT_CYCLE = "2";


    /**
     * 计费周期时间：与入住同日
     */
    String CHECK_IN_DATE = "1";
    /**
     * 计费周期时间：固定日
     */
    String FIXED_DATE = "2";


    /**
     * 优惠类型：预存优惠
     */
    String RESELLERS_DISCOUNTS = "1";
    /**
     * 优惠类型：普通优惠
     */
    String NORMAL_DISCOUNTS = "2";


    /**
     * 欠缴情况要求：仅允许在上个账单存在未缴金额
     */
    String HAVE_NOT_PAY = "1";
    /**
     * 欠缴情况要求：在任何账单都不存在未缴金额
     */
    String HAVE_PAY = "2";
    /**
     * 欠缴情况要求：不限
     */
    String UNLIMITED = "3";


    /**
     * 优惠规则:减免月份
     */
    String DISCOUNT_ON_MONTH = "1";
    /**
     * 优惠规则:折扣
     */
    String DISCOUNTS = "2";


    /**
     * 订单状态:已到账
     */
    String RECEIVED = "1";

    /**
     * 订单状态:未到账
     */
    String UNPAID = "0";
}
