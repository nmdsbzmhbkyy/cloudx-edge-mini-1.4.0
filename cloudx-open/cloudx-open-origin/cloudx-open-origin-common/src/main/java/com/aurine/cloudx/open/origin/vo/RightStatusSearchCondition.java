package com.aurine.cloudx.open.origin.vo;

import lombok.Data;

@Data
public class RightStatusSearchCondition {

    /**
     * 所要查询的人员ID
     * */
    private String personId;

    /**
     * 所要查询的人员姓名
     * */
    private String certMedia;

    /**
     * 所要查询的人员类型
     * */
    private String dlStatus;

}
