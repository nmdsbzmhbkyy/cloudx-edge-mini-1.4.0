package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectShiftPlan;
import com.aurine.cloudx.estate.entity.ProjectStaffShiftDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Title: ProjectShiftPlanFromVo
 * Description: 排班计划表单Vo
 *
 * @author guhl@aurine.cn
 * @version 1.0.0
 * @date 2020/7/31 14:50
 */
@Data
@ApiModel(value = "排班计划表单对象")
public class ProjectShiftPlanFromVo extends ProjectShiftPlan {
    /**
     * 员工id集合
     */
    @ApiModelProperty("员工id集合")
    List<String> staffIds;

    /**
     * 员工排班明细
     */
    @ApiModelProperty("员工排班明细")
    List<ProjectStaffShiftDetail> projectStaffShiftDetails;
}
