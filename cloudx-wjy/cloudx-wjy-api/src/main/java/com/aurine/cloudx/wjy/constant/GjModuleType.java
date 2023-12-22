package com.aurine.cloudx.wjy.constant;

import lombok.Getter;

/**
 * @author ： huangjj
 * @date ： 2021/5/13
 * @description： 管家模块类型
 */
public enum GjModuleType {
    WaterMeter("/meter/1", "水表"),
    ElectricityMeter("/meter/2", "电表"),
    GasMeter("/meter/3", "燃气"),
    Query("/query", "催缴"),
    ;

    @Getter
    private final String type;
    @Getter
    private final String desc;


    GjModuleType(String t, String desc) {//String n,
        //name = n;
        this.type = t;

        this.desc = desc;
    }
}