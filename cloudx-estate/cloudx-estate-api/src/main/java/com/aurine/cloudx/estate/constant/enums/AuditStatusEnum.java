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
public enum AuditStatusEnum {

    /**
     * 待业主审批
     */
    waitAudit("0"),
    /**
     * 审批中（待物业审核）
     */
    inAudit("1"),
    /**
     * 已通过
     */
    pass("2"),

    /**
     * 重新下发
     */
    reissue("8"),
    /**
     * 未通过
     */
    notPass("9");


    public String code;

}
