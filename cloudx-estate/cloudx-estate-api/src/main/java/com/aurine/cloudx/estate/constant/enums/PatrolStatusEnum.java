package com.aurine.cloudx.estate.constant.enums;

import lombok.AllArgsConstructor;

/**
 * <p>巡更状态</p>
 * @ClassName: PatrolStatusEnum
 * @author: 黄阳光 <huangyg@aurine.cn>
 * @date: 2020/9/29 19:05=
 */
@AllArgsConstructor
public enum PatrolStatusEnum {
    /**
     * 未巡更
     */
    NOPETROL("0"),
    /**
     * 巡更中
     */
    ONPATROL("1"),
    /**
     * 已巡更
     */
    PATROLCOMPLETED("2"),
    /**
     * 已过期
     */
    OVERDUE("3");

    public String code;
}
