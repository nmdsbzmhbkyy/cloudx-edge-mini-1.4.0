package com.aurine.cloudx.estate.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @Author: wrm
 * @Date: 2022/05/16 14:13
 * @Package: com.aurine.openv2.vo
 * @Version: 1.0
 * @Remarks: 部门对象vo，返回部门及父级部门信息
 **/
@Data
@ApiModel(value = "部门信息dto")
public class OpenApiSysProjectDeptDto {

	/**
	 * 关联pgixx.sys_dept.dept_id
	 */
	@ApiModelProperty(value = "关联pgixx.sys_dept.dept_id")
	private Integer deptId;

	/**
	 * 租户ID
	 */
	@ApiModelProperty("租户ID")
	private Integer tenantId;

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
	private Integer operator;

	/**
	 * 部门负责人
	 */
	@ApiModelProperty(value = "部门负责人")
	private String principal;

	//------------额外参数-------------
	/**
	 * 部门总人数
	 */
	@ApiModelProperty(value = "部门总人数")
	private Integer staffCount;
}
