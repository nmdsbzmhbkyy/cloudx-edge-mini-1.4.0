package com.aurine.cloudx.open.origin.constant.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PositionEnum {
    /**
     * 员工
     */
    STAFF("1","员工"),

    /**
     * 经理
     */
    MANAGER("2", "经理"),
    /**
     * 负责人
     */
    PERSON_IN_CHANGE("99", "负责人");


    public String code;
    public String name;
}
