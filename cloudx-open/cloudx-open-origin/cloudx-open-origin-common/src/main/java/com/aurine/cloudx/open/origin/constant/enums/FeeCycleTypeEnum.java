package com.aurine.cloudx.open.origin.constant.enums;

import lombok.AllArgsConstructor;

/**
 * Title: FeeConstant
 * Description: 收费周期类型
 *
 * @author xull@aurine.cn
 * @version 1.0.0
 * @date 2020/7/22 13:09
 */
@AllArgsConstructor
public enum FeeCycleTypeEnum {
    /**
     * 一个月
     */
    ONE_MONTH("1",1),
    /**
     * 两个月
     */
    TWO_MONTH("2",2),
    /**
     * 三个月
     */
    THREE_MONTH("3",3),
    /**
     * 半年
     */
    HALF_YEAR("4",6),
    /**
     * 一年
     */
    ONE_YEAR("5",12);

    public String code;
    public Integer value;

    public static Integer getValueByCode(String code) {
        Integer value = 1;
        switch (code){
            case "1":
                value = 1;
                break;
            case "2":
                value=2;
                break;
            case "3":
                value = 3;
                break;
            case "4":
                value = 6;
                break;
            case "5":
                value = 12;
                break;
            default:
                value = 1;
        }
        return value;
    }
}
