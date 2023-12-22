package com.aurine.cloudx.estate.open.staff.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@ApiModel("部门管理查询条件")
@Data
public class SysProjectDeptPage extends Page {
    
    @ApiModelProperty("部门名称")
    private String deptName;

}
