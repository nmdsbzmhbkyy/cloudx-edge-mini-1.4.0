package com.aurine.cloudx.estate.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 卡状态
 */
@AllArgsConstructor
@Getter
public enum CardStateEnum {


    /**
     * 成功
     */
    SUCCESS("0"),

    /**
     * 进行中
     */
    ONGOING("1"),

    /**
     * 失败
     */
    FAIL("2");

    public String code;

}

