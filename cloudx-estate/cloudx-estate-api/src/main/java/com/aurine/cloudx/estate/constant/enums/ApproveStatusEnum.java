package com.aurine.cloudx.estate.constant.enums;

import lombok.AllArgsConstructor;

/**
 * (ApproveStatusEnum)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/9/2 8:49
 */
@AllArgsConstructor
public enum ApproveStatusEnum {
    /**
     * 未处理
     */
    untreated("0"),
    /**
     * 处理中
     */
    processing("1"),

    /**
     * 已完成
     */
    finish("2");

    public String code;
}
