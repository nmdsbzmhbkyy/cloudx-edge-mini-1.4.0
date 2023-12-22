package com.aurine.cloudx.estate.constant.enums;

import lombok.AllArgsConstructor;
/**
 * <p>车位归属类型</p>
 * @ClassName: ParkingPlaceOwnTypeEnum
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/11 15:23
 * @Copyright:
 */
@AllArgsConstructor
public enum ParkingPlaceOwnTypeEnum {
    /**
     * 租赁
     */
    RENT(2),
    /**
     * 产权
     */
    OWNED(1),
    /**
     * 闲置
     */
    FREE(0);

    public int code;
}
