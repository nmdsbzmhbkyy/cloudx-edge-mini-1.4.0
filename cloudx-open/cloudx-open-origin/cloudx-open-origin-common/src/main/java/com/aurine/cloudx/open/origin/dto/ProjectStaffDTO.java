package com.aurine.cloudx.open.origin.dto;

import com.aurine.cloudx.open.origin.entity.ProjectStaff;
import com.aurine.cloudx.open.origin.vo.ProjectPersonAttrListVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;

@Data
@ApiModel(value = "员工传输对象")
@EqualsAndHashCode(callSuper = true)
public class ProjectStaffDTO extends ProjectStaff {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "角色id")
    private Integer oldRoleId;
    
    @ApiModelProperty(value = "新的角色id")
    private Integer newRoleId;
    
    @ApiModelProperty(value = "过期时间")
    private LocalDate roleExpTime;

    @ApiModelProperty(value = "用户拓展属性")
    private List<ProjectPersonAttrListVo> projectPersonAttrListVos;
}
