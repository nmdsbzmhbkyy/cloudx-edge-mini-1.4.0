package com.aurine.cloudx.estate.entity;

import lombok.Getter;

/**
 * @author ： huangjj
 * @date ： 2021/5/13
 * @description： 我家模块类型
 */
public enum WjModuleType {
    chargeSetIndex("/menus/toMain/chargeSetIndex", "收费设置"),
    User("/user", "用户中心"),
    Bill("/user/bill/0", "查询缴费"),
    WaterMeter("/meter/1", "水表"),
    ElectricityMeter("/meter/2", "电表"),
    GasMeter("/meter/3", "燃气"),
    Query("/query", "催缴"),
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