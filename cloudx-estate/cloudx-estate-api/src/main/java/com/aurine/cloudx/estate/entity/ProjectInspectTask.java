package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 设备巡检任务(ProjectInspectTask)表实体类
 *
 * @author 王良俊
 * @since 2020-07-28 17:06:49
 */
@Data
@TableName("project_inspect_task")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "设备巡检任务(ProjectInspectTask)")
public class ProjectInspectTask extends Model<ProjectInspectTask> {

    private static final long serialVersionUID = 247686483178564833L;



    /**
     * 巡检任务id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "巡检任务id")
    private String taskId;

    /**
     * 巡检计划id
     */
    @ApiModelProperty(value = "巡检计划id")
    private String inspectPlanId;

    /**
     * 巡检计划名称
     */
    @ApiModelProperty(value = "巡检计划名称")
    private String inspectPlanName;

    /**
     * 计划巡检时间
     */
    @ApiModelProperty(value = "计划巡检时间")
    private String planInspectTime;

    /**
     * 巡检路线名称
     */
    @ApiModelProperty(value = "巡检路线名称")
    private String inspectRouteName;

    /**
     * 计划巡检人，逗号分隔
     */
    @ApiModelProperty(value = "计划巡检人，逗号分隔")
    private String planStaff;


    /**
     * 顺序要求 0 无要求 1 有要求
     */
    @ApiModelProperty(value = "顺序要求 0 无要求 1 有要求")
    private String isSort;

    /**
     * 巡检状态 参考字典类型 inspect_status
     */
    @ApiModelProperty(value = "巡检状态 0: '待巡检' 1: '巡检中' '已完成' 3: '已取消' 4: '已过期' 参考字典类型 inspect_status")
    private String status;

    /**
     * 实际执行人员，逗号分隔
     */
    @ApiModelProperty(value = "实际执行人员，逗号分隔")
    private String execStaff;

    /**
     * 巡检结果 参考字典类型 inspect_result 0 未巡检 1 正常 2 异常
     */
    @ApiModelProperty(value = "巡检结果 参考字典类型 inspect_result  0 未巡检 1 正常 2 异常")
    private String result;

    /**
     * 实际巡检时间
     */
    @ApiModelProperty(value = "实际巡检时间")
    private String inspectTime;

    /**
     * 实际耗时（分钟）
     */
    @ApiModelProperty(value = "实际耗时（分钟）")
    private Integer timeElapsed;

    /**
     * 巡检情况 参考字典类型 check_in_status 1 正常 0 超时
     */
    @ApiModelProperty(value = "巡检情况 参考字典类型 check_in_status 1 正常 0 超时")
    private String checkInStatus;

    /**
     * 任务编码
     * */
    @ApiModelProperty(value = "任务编码")
    private String inspectTaskCode;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private Integer operator;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

}