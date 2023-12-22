package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectStaffShiftDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Title: ProjectStaffShiftDetailPageVo
 * Description:人员排班调整分页vo
 *
 * @author guhl@aurine.cn
 * @version 1.0.0
 * @date 2021/3/11 15:16
 */
@Data
@ApiModel("人员排班调整分页vo")
public class ProjectStaffShiftDetailPageVo extends ProjectStaffShiftDetail {
    @ApiModelProperty("员工姓名")
    private String staffName;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("部门id")
    private String deptId;


}
