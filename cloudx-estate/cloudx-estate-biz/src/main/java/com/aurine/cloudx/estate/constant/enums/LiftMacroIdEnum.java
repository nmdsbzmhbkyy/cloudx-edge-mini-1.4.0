package com.aurine.cloudx.estate.constant.enums;

import lombok.AllArgsConstructor;

/**
 * <p> 电梯逻辑通行策略宏类型</p>
 */
@AllArgsConstructor
public enum LiftMacroIdEnum {
    FLOOR_BELONG_HOUSE("所属楼栋所在楼层权限"),
    FLOOR_BELONG_HOUSE_AND_PUBLIC_FLOORS_IN_BUILDING("所属楼栋所有公共楼层权限"),
    ALL_FLOOR_IN_BUILDING("所属楼栋所有楼层权限"),
    ALL_FLOOR_ANYWHERE("全部楼栋所有梯控所有楼层权限")
    ;
    public String note;


}
