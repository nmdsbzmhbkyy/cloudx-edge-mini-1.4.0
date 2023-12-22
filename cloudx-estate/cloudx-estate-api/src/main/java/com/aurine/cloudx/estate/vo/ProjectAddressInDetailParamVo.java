package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * (ProjectAddressInDetailParamVo)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/9/1 9:31
 */
@Data
@ApiModel(value = "地址详细查询条件")
public class ProjectAddressInDetailParamVo {
    @ApiModelProperty("经度")
    private Double lon;
    @ApiModelProperty("纬度")
    private Double lat;
//    @ApiModelProperty("高度")
//    private Double alt;
}
