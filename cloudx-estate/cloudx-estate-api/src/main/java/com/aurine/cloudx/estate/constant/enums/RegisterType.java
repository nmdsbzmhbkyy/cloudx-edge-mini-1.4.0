package com.aurine.cloudx.estate.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RegisterType {
    PROJECT("项目级",0),
    DEVICE("设备级",1)
    ;
    private String name;
    private Integer code;

}
