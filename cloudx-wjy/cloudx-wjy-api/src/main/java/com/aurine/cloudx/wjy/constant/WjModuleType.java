package com.aurine.cloudx.wjy.constant;

import lombok.Getter;

/**
 * @author ： huangjj
 * @date ： 2021/5/13
 * @description： 我家模块类型
 */
public enum WjModuleType {
    User("/user", "用户中心"),
    Bill("/user/bill/0", "查询缴费"),
    ;

    @Getter
    private final String type;
    @Getter
    private final String desc;


    WjModuleType(String t, String desc) {//String n,
        //name = n;
        this.type = t;

        this.desc = desc;
    }
}