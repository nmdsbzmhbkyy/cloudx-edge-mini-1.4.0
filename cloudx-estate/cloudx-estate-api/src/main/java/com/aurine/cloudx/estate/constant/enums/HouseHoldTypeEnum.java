package com.aurine.cloudx.estate.constant.enums;

import lombok.AllArgsConstructor;

/**
 * <p>住户类型</p>
 * @ClassName: HouseHoldTypeEnum
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/6/16 8:55
 * @Copyright:
 */
@AllArgsConstructor
public enum HouseHoldTypeEnum {
    /**
     * 业主
     */
    OWNER("1"),
    /**
     * 家属
     */
    FAMILY ("2"),
    /**
     * 民宿
     */
    TENANT("3");

    public String code;
}
