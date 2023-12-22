package com.aurine.cloudx.estate.constant.enums;

import lombok.AllArgsConstructor;

/**
 * @ClassName: PlaceRelTypeEnum
 * @author: 王良俊
 * @date: 2020年05月09日 上午10:40:00
 * @Copyright:
 */
@AllArgsConstructor
public enum PlaceRelTypeEnum {

    /**
     * 公共
     */
    PUBLIC("0"),
    /**
     * 产权
     */
    PROPERTYRIGHT("1"),
    /**
     * 租赁
     */
    RENT("2");
    public String code;

}
