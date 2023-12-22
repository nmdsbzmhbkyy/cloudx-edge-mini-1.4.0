package com.aurine.cloudx.estate.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: wrm
 * @Date: 2022/05/24 10:33
 * @Package: com.aurine.openv2.dto
 * @Version: 1.0
 * @Remarks:
 **/
@Data
@ApiModel(value = "人脸信息dto")
public class OpenApiProjectFaceResourcesDto {

	/**
	 * 人脸id，uuid
	 */
	@ApiModelProperty(value = "人脸id，uuid")
	private String faceId;
	/**
	 * 人脸编号，第三方传入
	 */
	@ApiModelProperty(value = "人脸编号，第三方传入")
	private String faceCode;
	/**
	 * 人脸名称
	 */
	@ApiModelProperty(value = "人脸名称")
	private String faceName;
	/**
	 * 人员类型 1 住户 2 员工 3 访客
	 */
	@ApiModelProperty(value = "人员类型 1 住户 2 员工 3 访客")
	private String personType;

	/**
	 * 图片来源 1 web端 2 小程序 3 app
	 */
	@ApiModelProperty(value = "图片来源 1 web端 2 小程序 3 app")
	private String origin;
	/**
	 * 人员id, 根据人员类型取对应表id
	 */
	@ApiModelProperty(value = "人员id, 根据人员类型取对应表id")
	private String personId;
	/**
	 * (入参)图片地址
	 */
	@ApiModelProperty(value = "图片地址")
	private String picUrl;
	/**
	 * 状态 1 正常 2 冻结
	 */
	@ApiModelProperty(value = "状态 1 正常 2 冻结")
	private String status;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createTime;
	/**
	 * 更新时间
	 */
	@ApiModelProperty(value = "更新时间")
	private LocalDateTime updateTime;

	/**
	 * 项目Id
	 */
	@ApiModelProperty(value = "项目Id")
	private Integer projectId;

	/**
	 * 手机号
	 */
	@ApiModelProperty(value = "手机号")
	private String mobile;

    /**
     * 通行方案id
     */
    @ApiModelProperty(value = "通行方案id")
    private String planId;
}

