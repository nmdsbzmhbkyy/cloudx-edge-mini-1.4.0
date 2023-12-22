package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectStaff;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ProjectPatrolAssignVo {

    @ApiModelProperty("巡更记录ID")
    String patrolId;

    @ApiModelProperty("指派的人员（员工）对象列表")
    List<ProjectStaff> staffList;
}
