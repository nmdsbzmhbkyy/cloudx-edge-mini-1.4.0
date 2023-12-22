package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
public class ProjectInspectPlanShiftStaff extends OpenBasePo<ProjectInspectPlanShiftStaff> {

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


}