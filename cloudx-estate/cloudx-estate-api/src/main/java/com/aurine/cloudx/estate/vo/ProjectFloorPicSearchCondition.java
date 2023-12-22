package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProjectFloorPicSearchCondition {

	@ApiModelProperty(value = "项目编码")
	private Integer projectId;
	
	@ApiModelProperty(value = "平面图名称")
	private String picName;

	@ApiModelProperty(value = "节点ID")
	private String regionId;

	@ApiModelProperty(value = "设备ID")
	private String deviceId;
}
