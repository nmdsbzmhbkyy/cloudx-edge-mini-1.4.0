package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectInspectPlan;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

/**
 * 设备巡检计划设置(ProjectInspectPlan)表实体类
 *
 * @author 王良俊
 * @since 2020-07-23 18:34:04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("project_inspect_plan")
@ApiModel(value = "设备巡检计划vo对象")
public class ProjectInspectPlanVo extends ProjectInspectPlan {

    /**
     * 签到方式 参考字典 inspect_check_in_type，逗号分隔
     */
    @ApiModelProperty(value = "签到方式 1.二维码 2.现场拍照 参考字典 inspect_check_in_type，逗号分隔")
    private String[] checkInTypeArr;

    /**
     * 班次vo对象列表
     */
    @ApiModelProperty(value = "班次列表")
    private Map<String, List<ProjectInspectPlanShift3Vo>> planShiftMap;

    /**
     * 制定人姓名
     */
    @ApiModelProperty(value = "制定人姓名")
    private String operatorName;

    /**
     * 执行人姓名
     */
    @ApiModelProperty(value = "执行人姓名（多人）")
    private String staffNames;

    /**
     * 起始结束时间
     */
    @ApiModelProperty(value = "起始结束时间")
    private String beginEndTime;

    /**
     * 路线名
     */
    @ApiModelProperty(value = "路线名")
    private String inspectRouteName;

    /**
     * 制定日期
     */
    @ApiModelProperty(value = "制定日期")
    private String createTimeStr;

}