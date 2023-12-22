package com.aurine.cloudx.open.origin.constant.enums;

import lombok.AllArgsConstructor;

/**
 * Title: HousePersonRelTypeEnum
 * Description: 楼栋业主关系类型
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/4/22 9:19
 */
@AllArgsConstructor
public enum HousePersonRelTypeEnum {
    /**
     * 自住
     */
    PROPRIETOR("1"),
    /**
     * 租赁
     */
    TENANT("2"),
    /**
     * 民宿
     */
    BNB("3"),
    /**
     * 其他
     */
    OTHER("4");

    public String code;
}
