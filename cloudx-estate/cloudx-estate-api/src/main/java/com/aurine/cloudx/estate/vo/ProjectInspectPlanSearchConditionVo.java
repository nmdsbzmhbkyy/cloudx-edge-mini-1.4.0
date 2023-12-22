package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author 王良俊
 * @since 2020-07-23 18:34:04
 */
@Data
@ApiModel(value = "巡检计划查询条件")
public class ProjectInspectPlanSearchConditionVo {

    /**
     * 制定时间范围
     */
    private String[] timeRange;

    /**
     * 关键字
     */
    private String keyword;

    /**
     * 执行周期
     */
    private String cronType;

    /**
     * 状态 0 已停用 1 已启用
     */
    private String status;
}