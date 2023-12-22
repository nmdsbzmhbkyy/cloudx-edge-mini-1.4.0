package com.aurine.cloudx.wjy.constant;

import lombok.Getter;

/**
 * @author ： huangjj
 * @date ： 2021/5/13
 * @description： 资源分类
 */
public enum CatalogType {

    House(1,"房源"),
    Place(2,"场地"),
    AdPlace(3,"广告位"),
    ParkingPlace(4,"车位")
    ;

    @Getter
    private final Integer type;
    @Getter
    private final String desc;


    CatalogType(Integer t, String desc) {//String n,
        //name = n;
        this.type = t;

        this.desc = desc;
    }
}
