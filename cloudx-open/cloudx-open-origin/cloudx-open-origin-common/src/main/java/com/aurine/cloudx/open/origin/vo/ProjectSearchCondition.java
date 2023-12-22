package com.aurine.cloudx.open.origin.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>项目列表检索条件</p>
 * @ClassName: ProjectSearchCondition   
 * @author: wangwei<wangwei@aurine.cn>
 * @date: 2019年12月3日
 * @Copyright:
 */
@Data
public class ProjectSearchCondition {

	@ApiModelProperty(value = "项目名称/简称")
	private String projectName;
	@ApiModelProperty(value = "项目类型 1 智慧园区 2 其他")
	private String projectType;
	@ApiModelProperty(value = "项目所属企业")
	private String companyCode;
	@ApiModelProperty(value = "省编码")
	private String proviceCode;
	@ApiModelProperty(value = "审核状态")
	private String auditStatus;

}
