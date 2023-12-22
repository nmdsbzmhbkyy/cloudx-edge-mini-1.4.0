package com.aurine.cloudx.open.origin.constant.enums;

import lombok.AllArgsConstructor;

/**
 * <p>房屋与停车位的迁入迁出</p>
 * @ClassName: HouseParkingHistoryActionEnum
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/13 16:01
 * @Copyright:
 */
@AllArgsConstructor
public enum HouseParkingHistoryActionEnum {
    /**
     * 迁出
     */
    OUT("0"),
    /**
     * 迁入
     */
    IN("1"),

    /**
     * 审核迁出
     */
    VERIFYOUT("2"),

    /**
     * 审核迁入
     */
    VERIFYIN("3");

    public String code;
}
