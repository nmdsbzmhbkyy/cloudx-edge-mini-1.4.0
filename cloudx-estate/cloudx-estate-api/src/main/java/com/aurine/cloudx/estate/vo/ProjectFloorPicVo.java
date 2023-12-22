package com.aurine.cloudx.estate.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
public class ProjectFloorPicVo {

	@ApiModelProperty(value = "序号")
	private Integer seq;

	@ApiModelProperty(value = "平面图UID")
	private String picId;
	
	@ApiModelProperty(value = "项目编码")
	private Integer projectId;
	
	@ApiModelProperty(value = "平面图名称")
	private String picName;

	@ApiModelProperty(value = "上传人")
	private String userName;

	@ApiModelProperty(value = "分辨率")
	private String picResolution;

	@ApiModelProperty(value = "上传时间")
	private String createTime;

	@ApiModelProperty(value = "路径地址")
	private String accessPath;

	@ApiModelProperty(value = "区域ID")
	private String regionId;
	
	@ApiModelProperty(value = "上传人uid")
	private Integer operator;

	@ApiModelProperty(value = "纬度")
	private double lat;

	@ApiModelProperty(value = "经度")
	private double lon;

	
}
