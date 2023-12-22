package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * (ProjectAddressParamVo)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/9/1 8:57
 */
@Data
@ApiModel(value = "地址查询条件")
public class ProjectAddressParamVo {

    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("省份编码")
    private String provinceCode;
    @ApiModelProperty("市级编码")
    private String cityCode;
    @ApiModelProperty("县(区)编码")
    private String countyCode;
    @ApiModelProperty("街道编码")
    private String streetCode;
    @ApiModelProperty(value = "经度，保留小数点后6位")
    private Double lon;
    @ApiModelProperty(value = "纬度，保留小数点后6位")
    private Double lat;
    @ApiModelProperty("用户id")
    private Integer userId;
    @ApiModelProperty("类型")
    private String type;
}
