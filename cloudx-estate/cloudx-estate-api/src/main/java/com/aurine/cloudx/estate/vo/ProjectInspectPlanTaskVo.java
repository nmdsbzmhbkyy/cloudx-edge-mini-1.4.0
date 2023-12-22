package com.aurine.cloudx.estate.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 设备巡检计划设置(ProjectInspectPlan)表实体类
 *
 * @author 王良俊
 * @since 2020-07-23 18:34:04
 */
@Data
@ApiModel(value = "设备巡检计划vo对象")
public class ProjectInspectPlanTaskVo {

    /**
     * 计划id，uuid
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "计划id，uuid")
    private String planId;

    /**
     * 计划名称
     */
    @ApiModelProperty(value = "计划名称")
    private String planName;

    /**
     * 线路id
     */
    @ApiModelProperty(value = "线路id")
    private String inspectRouteId;

    /**
     * 签到方式 参考字典 inspect_check_in_type，逗号分隔
     */
    @ApiModelProperty(value = "签到方式 1.二维码 2.现场拍照 参考字典 inspect_check_in_type，逗号分隔")
    private String checkInType;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endTime;

    /**
     * 执行周期类型 1 按天 2 按周 3 自定义
     */
    @ApiModelProperty(value = "执行周期类型 1 按天 2 按周 3 自定义")
    private String cronType;

    /**
     * 周期间隔类型 1 连续 2 间隔1
     */
    @ApiModelProperty(value = "周期间隔类型 1 连续 2 间隔1")
    private String gapType;

    /**
     * 遇周六日 1 跳过 2 正常执行
     */
    @ApiModelProperty(value = "遇周六日 1 跳过 2 正常执行")
    private String ifSatOrSun;

    /**
     * 班次vo对象列表
     */
    @ApiModelProperty(value = "班次列表")
    private Map<String, List<ProjectInspectPlanShift3Vo>> planShiftMap;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private Integer operator;

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
     * 制定时间
     */
    @ApiModelProperty(value = "制定时间")
    private LocalDateTime createTime;

    /**
     * 当前状态 0 已停用 1 已启用
     */
    @ApiModelProperty(value = "当前状态 0 已停用 1 已启用")
    private char status;

    /**
     * 起始结束时间
     */
    @ApiModelProperty(value = "起始结束时间")
    private String beginEndTime;


}