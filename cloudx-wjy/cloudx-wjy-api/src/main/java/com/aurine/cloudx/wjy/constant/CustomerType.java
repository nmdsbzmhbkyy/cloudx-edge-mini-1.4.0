package com.aurine.cloudx.wjy.constant;

import lombok.Getter;

public enum CustomerType{
    //客户类型（P个人，E企业，G政府机构，S个体户，O其他企业，T临时）
    // Individuals/Personal, Enterprises, Government agencies, SelfEmployed, OtherEnterprises, Temporary
    CustomerTypePersonal( "P","个人"),
    CustomerTypeEnterprises( "E","企业"),
    CustomerTypeGovernment( "G", "政府机构"),
    CustomerTypeSelfEmployed( "S", "个体户"),
    CustomerTypeOtherEnterprises("O", "其他企业"),
    CustomerTypeTemporary("T", "临时"),
    ;
    //@Getter
    //private final String name;
    @Getter
    private final String type;
    @Getter
    private final String desc;


    CustomerType(String t, String desc) {//String n,
        //name = n;
        this.type = t;

        this.desc = desc;
    }
}