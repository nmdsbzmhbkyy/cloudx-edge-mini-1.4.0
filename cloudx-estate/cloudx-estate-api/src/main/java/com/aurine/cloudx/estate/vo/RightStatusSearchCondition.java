package com.aurine.cloudx.estate.vo;

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


    /**
     * 卡操作  1 下发 2 挂失 3 解挂 4 注销 5 换卡
     */
    private String cardStatus;


}
