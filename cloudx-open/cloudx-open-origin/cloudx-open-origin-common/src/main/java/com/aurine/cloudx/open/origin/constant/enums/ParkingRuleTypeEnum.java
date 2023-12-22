package com.aurine.cloudx.open.origin.constant.enums;

import lombok.AllArgsConstructor;

/**
 * <p>停车场计费规则类型</p>
 * @ClassName: ParkingRuleTypeEnum
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/7/13 9:01
 * @Copyright:
 */
@AllArgsConstructor
public enum ParkingRuleTypeEnum {
    /**
     * 临时车
     */
    TEMP("3"),
    /**
     * 月租车
     */
    MONTH("2"),
    /**
     * 免费车
     */
    FREE("1");

    public String code;
}
