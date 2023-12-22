package com.aurine.cloudx.estate.constant.enums;

import lombok.AllArgsConstructor;

/**
 * @ClassName: PlaceActionTypeEnum
 * @author: 王良俊
 * @date: 2020年05月09日 上午11:42:00
 * @Copyright:
 */
@AllArgsConstructor
public enum PlaceActionTypeEnum {

    /**
     * 迁入
     */
    IMMIGRATION("1"),
    /**
     * 迁出
     */
    EMIGRATION("0");

    public String code;

}
