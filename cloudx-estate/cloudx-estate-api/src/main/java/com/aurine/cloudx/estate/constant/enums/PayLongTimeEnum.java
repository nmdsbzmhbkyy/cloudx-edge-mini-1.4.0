package com.aurine.cloudx.estate.constant.enums;

import lombok.AllArgsConstructor;

/**
 * Title: FeeConstant
 * Description: 预存时长枚举
 *
 * @author xull@aurine.cn
 * @version 1.0.0
 * @date 2020/7/27 13:49
 */
@AllArgsConstructor
public enum PayLongTimeEnum {
    /**
     * 三个月
     */
    TREE_MONTH(3),
    /**
     * 六个月
     */
    SIX_MONTH(6),
    /**
     * 一年
     */
    ONE_YEAR(12),
    /**
     * 一年半
     */
    ONE_HALF_YEAR(18),
    /**
     * 两年
     */
    TWO_YEAR(24);

    public Integer code;
}
