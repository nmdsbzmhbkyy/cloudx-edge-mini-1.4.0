package com.aurine.cloudx.estate.constant.enums;

import lombok.AllArgsConstructor;

/**
 * <p>
 * 审核状态枚举
 * </p>
 *
 * @ClassName: AuditStatusEnum
 * @author: 王良俊 <>
 * @date: 2020年06月04日 下午08:20:44
 * @Copyright:
 */
@AllArgsConstructor
public enum RegisterTypeEnum {

    /**
     * 物业登记
     */
    propertyRegistration("1"),
    /**
     * 住户申请
     */
    residentApplication("2"),
    /**
     * 自主申请
     */
    independentApplication("3");

    public String code;

}
