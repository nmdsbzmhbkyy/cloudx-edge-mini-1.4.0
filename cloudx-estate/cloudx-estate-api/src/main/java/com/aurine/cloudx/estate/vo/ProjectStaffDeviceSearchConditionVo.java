

package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>员工 权限 查询结果VO</p>
 *
 */
@Data
@ApiModel(value = "员工 权限VO")
public class ProjectStaffDeviceSearchConditionVo extends ProjectPersonDeviceVo {
    private static final long serialVersionUID = 1L;
    /**
     * 员工姓名
     */
    @ApiModelProperty(value="员工姓名")
    private String staffName;

    /**
     * 员工性别
     */
    @ApiModelProperty(value="员工性别")
    private String sex;

    /**
     * 部门名
     */
    @ApiModelProperty(value="部门名")
    private String deptName;

    /**
     * 员工岗位
     */
    @ApiModelProperty(value="员工岗位")
    private String staffPost;

    /**
     * 级别 1 经理  2 主管 3 普通员工
     */
    @ApiModelProperty(value="级别 1 经理  2 主管 3 普通员工")
    private String grade;

    /**
     * 通行状态
     */
    @ApiModelProperty(value="通行状态")
    private String rightStatus;
    /**
     * 项目ID
     */
    @ApiModelProperty(value="项目ID")
    private Integer projectId;
}
