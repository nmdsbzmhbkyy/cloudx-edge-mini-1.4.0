package com.aurine.cloudx.open.origin.constant.enums;

import lombok.AllArgsConstructor;


/**
 * Title: FeeTypeEnum
 * Description: 收费类型枚举
 *
 * @author xull@aurine.cn
 * @version 1.0.0
 * @date 2020/7/22 13:09
 */
@AllArgsConstructor
public enum FeeTypeEnum {
    /**
     * 管理费
     */
    ADMINISTRATIVE_FEE("1"),
    /**
     * 维修基金
     */
    MAINTENANCE_COST("2"),
    /**
     * 垃圾处理费
     */
    WASTE_DISPOSAL_FEE("3"),
    /**
     * 押金
     */
    GUARANTEE_DEPOSIT("4"),
    /**
     * 违约金
     */
    PENAL_SUM("5"),
    /**
     * 其他
     */
    OTHER("6");
    public String code;
}
