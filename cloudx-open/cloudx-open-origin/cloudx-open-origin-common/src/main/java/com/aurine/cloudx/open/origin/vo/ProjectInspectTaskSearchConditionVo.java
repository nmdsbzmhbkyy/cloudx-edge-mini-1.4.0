package com.aurine.cloudx.open.origin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: ProjectInspectTaskSearchConditionVo
 * @author: 王良俊 <>
 * @date: 2020年07月30日 下午06:33:52
 * @Copyright:
 */
@Data
@ApiModel(value = "设备巡检任务查询条件")
public class ProjectInspectTaskSearchConditionVo {

    /**
     * 计划名
     */
    @ApiModelProperty(value = "计划名")
    private String inspectPlanName;

    /**
     * 计划巡检日期 日期数组只精确到日
     */
    @ApiModelProperty(value = "计划巡检日期")
    private String[] dateRange;

    /**
     * 巡检情况
     */
    @ApiModelProperty(value = "巡检情况")
    private String checkInStatus;

    /**
     * 巡检状态 参考字典类型 inspect_status
     */
    @ApiModelProperty(value = "巡检状态 0 待巡检 1 巡检中 2 已完成 3 已取消 4 已过期 参考字典类型 inspect_status")
    private String status;


    /**
     * 巡检结果
     */
    @ApiModelProperty(value = "巡检结果")
    private String result;

    /**
     *  计划巡检日期 yyyy-MM
     */
    @ApiModelProperty(value = "巡检日期")
    private String date;

}