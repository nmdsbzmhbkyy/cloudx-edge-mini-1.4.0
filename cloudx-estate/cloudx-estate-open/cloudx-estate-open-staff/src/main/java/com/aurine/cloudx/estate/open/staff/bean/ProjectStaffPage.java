package com.aurine.cloudx.estate.open.staff.bean;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@ApiModel("员工管理查询条件")
@Data
public class ProjectStaffPage extends Page{

    /**
     * 归属部门ID
     */
    @ApiModelProperty(value = "归属部门ID")
    private Integer departmentId;

    /**
     * 级别 1 经理  2 主管 3 普通员工
     */
    @ApiModelProperty(value = "级别 1 经理  2 主管 3 普通员工")
    private String grade;

    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    private String staffName;

}