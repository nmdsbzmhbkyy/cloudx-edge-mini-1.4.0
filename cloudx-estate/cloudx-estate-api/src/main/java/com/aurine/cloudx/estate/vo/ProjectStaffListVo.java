package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * (ProjectStaffListVo)员工列表视图
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/9/1 15:45
 */
@Data
@ApiModel(value = "员工列表视图")
public class ProjectStaffListVo {
    /**
     * 员工姓名
     */
    @ApiModelProperty(value = "员工姓名")
    private String staffName;
    /**
     * Id
     */
    @ApiModelProperty(value = "ID")
    private String staffId;
    /**
     * 部门id
     */
    @ApiModelProperty(value = "部门id")
    private Integer deptId;
    /**
     * 部门名
     */
    @ApiModelProperty(value = "部门名")
    private String deptName;

    /**
     * 员工岗位
     */
    @ApiModelProperty(value = "员工岗位")
    private String staffPost;

    /**
     * 级别 1 经理  2 主管 3 普通员工
     */
    @ApiModelProperty(value = "级别 1 经理  2 主管 3 普通员工")
    private String grade;
    /**
     * 联系电话
     */
    @ApiModelProperty(value = "联系电话")
    private String mobile;
    /**
     * 单位联系电话
     */
    @ApiModelProperty("单位联系电话")
    private String employerPhone;
}
