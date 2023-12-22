package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("城市信息")
public class ProjectCityInfoVo {
    /**
     * 省编码
     */
    @ApiModelProperty(value = "省编码")
    private String provinceCode;
    /**
     * 市编码
     */
    @ApiModelProperty(value = "市编码")
    private String cityCode;

    /**
     * 省名称
     */
    @ApiModelProperty(value = "省名称")
    private String provinceName;
    /**
     * 市名称
     */
    @ApiModelProperty(value = "市名称")
    private String cityName;
}
