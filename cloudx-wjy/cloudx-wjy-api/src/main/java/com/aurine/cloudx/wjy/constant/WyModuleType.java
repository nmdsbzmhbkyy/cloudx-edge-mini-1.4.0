package com.aurine.cloudx.wjy.constant;

import lombok.Getter;

/**
 * @author ： huangjj
 * @date ： 2021/5/13
 * @description： 物业模块类型
 */
public enum WyModuleType {
    chargeSetIndex("/menus/toMain/chargeSetIndex", "收费设置"),
    toMain("/users/toMain/main", "首页"),
    ;

    @Getter
    private final String type;
    @Getter
    private final String desc;


    WyModuleType(String t, String desc) {//String n,
        //name = n;
        this.type = t;

        this.desc = desc;
    }
}