package com.aurine.cloudx.estate.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 开放平台内部项目房屋增值服务Dto
 *
 * @author : Qiu
 * @date : 2022/7/14 10:47
 */

@Data
@ApiModel(value = "开放平台内部项目房屋增值服务Dto")
public class OpenApiProjectHouseServiceDto {

	/**
	 * 房屋ID
	 */
	@JSONField(name = "houseId")
	@JsonProperty(value = "houseId")
	@ApiModelProperty(value = "房屋ID")
	private String houseId;

	/**
	 * 房屋ID列表
	 */
	@JSONField(name = "houseIdList")
	@JsonProperty(value = "houseIdList")
	@ApiModelProperty(value = "房屋ID列表")
	private List<String> houseIdList;

    /**
     * 增值服务ID
     */
    @JSONField(name = "serviceId")
    @JsonProperty(value = "serviceId")
    @ApiModelProperty(value = "增值服务ID")
    private String serviceId;

    /**
     * 增值服务ID列表
     */
    @JSONField(name = "serviceIdList")
    @JsonProperty(value = "serviceIdList")
    @ApiModelProperty(value = "增值服务ID列表")
    private List<String> serviceIdList;

    /**
     * 失效时间
     */
    @JSONField(name = "expTime", format = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty(value = "expTime")
    @ApiModelProperty(value = "失效时间")
    private LocalDateTime expTime;
}
