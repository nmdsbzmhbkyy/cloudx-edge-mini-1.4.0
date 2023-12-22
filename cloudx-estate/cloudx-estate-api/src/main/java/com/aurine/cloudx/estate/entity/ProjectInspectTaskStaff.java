package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

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
public class ProjectInspectTaskStaff extends Model<ProjectInspectTaskStaff> {

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


    /**
     * 租户id
     */
    @ApiModelProperty(value = "租户id")
    private Integer tenant_id;


    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private Integer operator;


    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;


    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;


}