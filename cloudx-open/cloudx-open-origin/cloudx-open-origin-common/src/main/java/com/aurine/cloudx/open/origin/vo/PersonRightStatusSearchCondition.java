package com.aurine.cloudx.open.origin.vo;

import lombok.Data;

@Data
public class PersonRightStatusSearchCondition {

    /**
     * 所要查询的人员姓名
     * */
    private String personName;

    /**
     * 所要查询的人员类型
     * */
    private String personType;

    /**
     * 是否异常 （1是，0否）
     * */
    private Integer isException;

}
