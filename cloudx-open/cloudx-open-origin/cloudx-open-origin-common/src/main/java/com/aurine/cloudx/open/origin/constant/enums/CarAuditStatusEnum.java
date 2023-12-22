package com.aurine.cloudx.open.origin.constant.enums;

import lombok.AllArgsConstructor;

/**
 * <p>
 * 车辆审核状态枚举
 * </p>
 * @author : 王良俊
 * @date : 2021-03-08 09:21:32
 */
@AllArgsConstructor
public enum CarAuditStatusEnum {

    /**
     * 认证中（待物业审核）
     */
    inAudit("1"),
    /**
     * 已登记
     */
    pass("2"),
    /**
     * 已拒绝
     */
    notPass("9");

    public String code;

}
