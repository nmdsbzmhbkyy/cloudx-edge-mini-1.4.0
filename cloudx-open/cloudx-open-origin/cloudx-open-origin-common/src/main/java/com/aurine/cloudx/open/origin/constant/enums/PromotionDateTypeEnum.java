package com.aurine.cloudx.open.origin.constant.enums;

import lombok.AllArgsConstructor;

/**
 * Title: PromotionDateTypeEnum
 * Description: 优惠预存月份枚举
 *
 * @author xull@aurine.cn
 * @version 1.0.0
 * @date 2020/7/22 13:19
 */
@AllArgsConstructor
public enum PromotionDateTypeEnum {

    TREE_MONTH("1"),
    SIX_MONTH("2"),
    ONE_YEAR("3"),
    ONE_HALF_YEAR("4"),
    TWO_YEAR("5");
    public String code;
}
