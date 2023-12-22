package com.aurine.cloudx.estate.constant.enums;

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
    TEMP("3","临时车"),
    /**
     * 月租车
     */
    MONTH("2","月租车"),
    /**
     * 免费车
     */
    FREE("1","免费车");

    public String code;
    public String value;

    public static ParkingRuleTypeEnum getByCode(String code) {
        for (ParkingRuleTypeEnum value : ParkingRuleTypeEnum.values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }

}
