package com.aurine.cloudx.open.origin.constant.enums;

import lombok.AllArgsConstructor;

/**
 * <p>入云申请状态枚举</p>
 * @author : 王良俊
 * @date : 2021-12-02 17:43:53
 */
@AllArgsConstructor
public enum IntoCloudStatusEnum {
    /**
     * 未入云
     */
    NOT_INTO_CLOUD('0'),
    /**
     * 待审核
     */
    PENDING_AUDIT('1'),
    /**
     * 已拒绝
     */
    REJECT('2'),
    /**
     * 已入云
     */
    INTO_CLOUD('3'),
    /**
     * 解绑中
     */
    UNBINDING('4'),
    /**
     * 撤销申请中
     */
    REVOKE_REQUEST('5'),
    ;
    public char code;
}
