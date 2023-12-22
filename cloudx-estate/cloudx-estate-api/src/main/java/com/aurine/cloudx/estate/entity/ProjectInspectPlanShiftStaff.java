package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 巡检计划班次执行人员列表(ProjectInspectPlanShiftStaff)表实体类
 *
 * @author 王良俊
 * @since 2020-07-27 10:37:22
 */
@Data
@TableName("project_inspect_plan_shift_staff")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "巡检计划班次执行人员列表(ProjectInspectPlanShiftStaff)")
public class ProjectInspectPlanShiftStaff extends Model<ProjectInspectPlanShiftStaff> {

    private static final long serialVersionUID = 631696226496604787L;


    @ApiModelProperty(value = "")
    private Integer seq;


    /**
     * 记录id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "记录id")
    private String recordId;


    /**
     * 员工id
     */
    @ApiModelProperty(value = "员工id")
    private String staffId;


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
    private Date createTime;


    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;


}