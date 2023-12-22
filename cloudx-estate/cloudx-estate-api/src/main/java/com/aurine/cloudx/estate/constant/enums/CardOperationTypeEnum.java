package com.aurine.cloudx.estate.constant.enums;

import lombok.AllArgsConstructor;

/**
 * 卡操作记录
 *
 * @author zy
 * @date 2022-10-18 08:40:49
 */
@AllArgsConstructor
public enum CardOperationTypeEnum {
    /**
     * 下发
     */
    SEND("1"),

    /**
     * 挂失
     */
    LOSE_CARD("2"),
    /**
     * 解挂
     */
    OBTAIN_CARD("3"),

    /**
     * 注销
     */
    REMOVE_CARD("4"),

    /**
     * 换卡
     */
    CHANGE_CARD("5"),

    /**
     * 禁用
     */
    DISABLE("6");


    public String code;
}
