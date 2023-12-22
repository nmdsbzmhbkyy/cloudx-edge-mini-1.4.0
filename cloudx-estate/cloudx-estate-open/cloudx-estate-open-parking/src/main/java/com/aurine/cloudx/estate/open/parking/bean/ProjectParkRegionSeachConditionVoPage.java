package com.aurine.cloudx.estate.open.parking.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "车位区域")
public class ProjectParkRegionSeachConditionVoPage extends Page {

    /**
     * 车位区域名
     */
    @ApiModelProperty(value = "车位区域名")
    private String parkRegionName;
    /**
     * 车位区域Id
     */
    @ApiModelProperty(value = "车位区域Id")
    private String parkRegionId;
    /**
     * 车位区域Id
     */
    @ApiModelProperty(value = "停车场Id")
    private String parkId;
    /**
     * 停车位总数
     */
    @ApiModelProperty(value = "停车位总数")
    private Integer parkNum;
    /**
     * 已使用车位数
     */
    @ApiModelProperty(value = "已使用车位数")
    private Integer usedPark;
    /**
     * 是否公共区域 1 是 0 否
     */
    @ApiModelProperty(value = "是否公共区域 1 是 0 否")
    private String isPublic;
}
