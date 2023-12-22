package com.aurine.cloudx.estate.constant.enums;

import lombok.AllArgsConstructor;

/**
 * <p>巡更状态</p>
 * @ClassName: PatrolStatusEnum
 * @author: 黄阳光 <huangyg@aurine.cn>
 * @date: 2020/9/29 19:05=
 */
@AllArgsConstructor
public enum PatrolPersonTypeEnum {
    /**
     * 计划人员
     */
    ONPATROL("1"),
    /**
     * 执行人员
     */
    PATROLCOMPLETED("2");

    public String code;
}
