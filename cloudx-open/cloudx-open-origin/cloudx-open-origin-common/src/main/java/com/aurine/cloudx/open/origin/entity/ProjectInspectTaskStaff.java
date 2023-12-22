package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 巡检任务人员列表，用于app推送和领取(ProjectInspectTaskStaff)表实体类
 *
 * @author 王良俊
 * @since 2020-10-26 11:41:49
 */
@Data
@TableName("project_inspect_task_staff")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "巡检任务人员列表，用于app推送和领取(ProjectInspectTaskStaff)")
public class ProjectInspectTaskStaff extends OpenBasePo<ProjectInspectTaskStaff> {

    private static final long serialVersionUID = -18662587854274357L;

    /**
     * 自增序列
     */
    @TableId
    @ApiModelProperty(value = "自增序列")
    private Integer seq;


    /**
     * 巡检任务id
     */
    @ApiModelProperty(value = "巡检任务id")
    private String taskId;


    /**
     * 员工id
     */
    @ApiModelProperty(value = "员工id")
    private String staffId;


    /**
     * 类型 1 计划人员 2 执行人员
     */
    @ApiModelProperty(value = "类型 1 计划人员 2 执行人员")
    private String staffType;


    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    private Integer projectId;


}