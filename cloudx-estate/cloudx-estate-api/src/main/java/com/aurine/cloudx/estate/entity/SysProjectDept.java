

package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 项目内部门信息
 *
 * @author lingang
 * @date 2020-05-07 18:44:46
 */
@Data
@TableName("sys_project_dept")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "项目内部门信息")
public class SysProjectDept extends Model<SysProjectDept> {
    private static final long serialVersionUID = 1L;

    /**
     * 关联pgixx.sys_dept.dept_id
     */
    @TableId(value = "deptId", type = IdType.INPUT)
    @ApiModelProperty(value = "关联pgixx.sys_dept.dept_id")
    private Integer deptId;
    /**
     * 项目ID
     */
    @ApiModelProperty(value = "项目ID")
    private Integer projectId;
    /**
     * 第三方编码
     */
    @ApiModelProperty(value = "第三方编码")
    private String deptCode;
    /**
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称")
    private String deptName;
    /**
     * 部门职能
     */
    @ApiModelProperty(value = "部门职能")
    private String deptDesc;
    /**
     * 联系电话
     */
    @ApiModelProperty(value = "联系电话")
    private String contactPhone;
    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    @TableField(fill = FieldFill.INSERT)
    private Integer operator;
    /**
     *
     */
    @ApiModelProperty(value = "")
    private LocalDateTime createTime;
    /**
     *
     */
    @ApiModelProperty(value = "")
    private LocalDateTime updateTime;
    /**
     * 联系电话
     */
    @ApiModelProperty(value = "部门负责人")
    private String principal;
}
