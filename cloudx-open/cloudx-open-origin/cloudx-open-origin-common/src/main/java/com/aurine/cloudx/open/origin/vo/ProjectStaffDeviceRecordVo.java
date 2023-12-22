

package com.aurine.cloudx.open.origin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 员工 权限 查询结果VO
 * </p>
 *
 * @author: 王良俊
 * @date: 2020/5/22 15:22
 * @Copyright:
 */
@Data
@ApiModel(value = "员工 权限VO")
public class ProjectStaffDeviceRecordVo {
    private static final long serialVersionUID = 1L;

    /**
     * 员工姓名
     */
    @ApiModelProperty(value="员工姓名")
    private String staffName;
    /**
     * Id
     */
    @ApiModelProperty(value="ID")
    private String staffId;

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
     * 启用状态
     */
    @ApiModelProperty(value="启用状态")
    private String isActive;
}
