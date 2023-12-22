package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 记录排班计划关联的参与人信息(ProjectShiftPlanStaff)表实体类
 *
 * @author guhl@aurine.cn
 * @since 2020-07-31 11:00:58
 */
@Data
@TableName("project_shift_plan_staff")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "记录排班计划关联的参与人信息(ProjectShiftPlanStaff)")
public class ProjectShiftPlanStaff extends OpenBasePo<ProjectShiftPlanStaff> {

    private static final long serialVersionUID = -65200206878093706L;

    /**
     * 关系id,uuid
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "关系id,uuid")
    private String relId;

    /**
     * 排班计划id
     */
    @ApiModelProperty(value = "排班计划id")
    private String planId;

    /**
     * 员工id
     */
    @ApiModelProperty(value = "员工id")
    private String staffId;


}