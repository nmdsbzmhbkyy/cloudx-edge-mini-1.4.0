package com.aurine.cloudx.estate.constant.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName: ParkingManageRelExcelEnum
 * @author: 邹宇 <>
 * @date:  2021年06月23日 下午02:45:23
 * @Copyright:
 */
@Getter
@AllArgsConstructor
public enum PerimeterStatusEnum {

    /**
     * 撤防
     */
    DISARM("0","撤防"),

    /**
     * 布防
     */
    ARMED("1","布防");

    /**
     * 状态类型
     */
    private final String type;

    /**
     * 状态名称
     */
    private final String name;


    /**
     * @param type
     * @return
     */
    public static PerimeterStatusEnum getEnum(String type) {
        for (PerimeterStatusEnum value : PerimeterStatusEnum.values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        return null;
    }
}
