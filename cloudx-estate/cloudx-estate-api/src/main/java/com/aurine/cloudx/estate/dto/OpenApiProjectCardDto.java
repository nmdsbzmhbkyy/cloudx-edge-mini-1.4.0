package com.aurine.cloudx.estate.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: wrm
 * @Date: 2022/05/23 15:10
 * @Package: com.aurine.openv2.dto
 * @Version: 1.0
 * @Remarks:
 **/
@Data
@ApiModel(value = "门禁卡信息Dto")
public class OpenApiProjectCardDto {
	/**
	 * 卡片uid
	 */
	@ApiModelProperty(value = "卡片uid")
	private String cardId;

	/**
	 * 卡号
	 */
	@ApiModelProperty(value = "卡号")
	private String cardNo;

	/**
	 * 卡号
	 */
	@ApiModelProperty(value = "卡号第三方编码")
	private String cardCode;

	/**
	 * 人员类型 1 住户 2 员工 3 访客
	 */
	@ApiModelProperty(value = "人员类型 1 住户 2 员工 3 访客")
	private String personType;

	/**
	 * 人员id，根据人员类型取对应表id。卡未使用时为空
	 */
	@ApiModelProperty(value = "人员id，根据人员类型取对应表id。卡未使用时为空")
	private String personId;

	/**
	 * 状态 0 未使用 1 使用中 2 冻结
	 */
	@ApiModelProperty(value = "状态 0 未使用 1 使用中 2 冻结")
	private String status;

	/**
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人")
	private Integer operator;

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
	 * 手机号
	 */
	@ApiModelProperty(value = "手机号")
	private String mobile;

	@ApiModelProperty(value = "项目Id")
	private Integer projectId;

    /**
     * 通行方案id，为空则为自选权限
     */
    @ApiModelProperty(value = "通行方案id")
    private String planId;

}
