package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectStaff;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 员工属性视图(含拓展字段)
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProjectStaffVo extends ProjectStaff {
    @ApiModelProperty("员工拓展属性")
    List<ProjectPersonAttrListVo> projectPersonAttrListVos ;
    @ApiModelProperty("部门名称")
    private String deptName;
}
