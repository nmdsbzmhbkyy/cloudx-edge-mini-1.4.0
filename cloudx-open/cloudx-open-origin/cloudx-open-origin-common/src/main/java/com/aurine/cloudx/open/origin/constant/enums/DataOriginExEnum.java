package com.aurine.cloudx.open.origin.constant.enums;

import lombok.AllArgsConstructor;

/**
 * (DataOriginExEnum)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/11/18 16:33
 */
@AllArgsConstructor
public enum DataOriginExEnum {
    /**
     * 未知
     */
    UNKNOWN("0"),
    /**
     * 物业
     */
    WY("1"),
    /**
     * 业主
     */
    YZ("2"),
    /**
     * 访客
     */
    FK("3"),
    ;

    public String code;
}
