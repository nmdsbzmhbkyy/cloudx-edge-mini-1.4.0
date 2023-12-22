package com.aurine.cloudx.edge.sync.biz.mq.enums;

import lombok.AllArgsConstructor;

/**
 * @author:zouyu
 * @data:2022/5/19 11:07 上午
 */
@AllArgsConstructor
public enum TypeEnum {

    DEVICE_ADD("device_add"),
    DEVICE_DELETE("device_delete"),
    PERSON_ADD("person_add"),
    PERSON_DELETE("person_delete");


    public String value;

}
