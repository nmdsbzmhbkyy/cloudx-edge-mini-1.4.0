package com.aurine.cloudx.wjy.constant;

import lombok.Getter;

public enum RoomProperty {

    House(0,"住宅"),
    ;

    @Getter
    private final Integer type;
    @Getter
    private final String desc;


    RoomProperty(Integer t, String desc) {//String n,
        //name = n;
        this.type = t;

        this.desc = desc;
    }
}
