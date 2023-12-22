package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectInspectTask;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 设备巡检任务(ProjectInspectTask)表实体类
 *
 * @author 王良俊
 * @since 2020-07-28 17:06:49
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "设备巡检任务Vo对象")
public class ProjectInspectTaskVo extends ProjectInspectTask {

    /**
     * 巡检路线id
     */
    @ApiModelProperty(value = "巡检路线id")
    private String inspectRouteId;

    /**
     * 计划巡检人员ID列表
     */
    @ApiModelProperty(value = "计划巡检人员ID列表")
    private String planStaffIds;

    /**
     * Cron表达式
     */
    @ApiModelProperty(value = "Cron表达式")
    private String frequency;

    /**
     * 执行周期类型
     */
    @ApiModelProperty(value = "执行周期类型")
    private String cronType;

    /**
     * '周期间隔类型 1 连续 2 间隔1'
     */
    @ApiModelProperty(value = "'周期间隔类型 1 连续 2 间隔1'")
    private String gapType;

    /**
     * 遇周六日 1 跳过 2 正常执行
     */
    @ApiModelProperty(value = "遇周六日 1 跳过 2 正常执行")
    private String ifSatOrSun;

    /**
     * 第三种自定义时间的原时间 因为转换成cron会丢失掉年份信息故做此备份
     */
    @ApiModelProperty(value = "第三种自定义时间的原时间")
    private String curDay;

    @Override
    public String toString() {
        return "ProjectInspectTaskVo{" +
                "inspectRouteId='" + inspectRouteId + '\'' +
                ", planStaffIds='" + planStaffIds + '\'' +
                ", frequency='" + frequency + '\'' +
                ", cronType='" + cronType + '\'' +
                ", gapType='" + gapType + '\'' +
                ", ifSatOrSun='" + ifSatOrSun + '\'' +
                ", curDay='" + curDay + '\'' +
                '}';
    }
}