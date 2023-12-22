package com.aurine.cloudx.open.origin.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 考勤地址视图
 */
@Data
public class ProjectAttendancePointPlaceVo {
    /**
     * 考勤点地址
     */
    @ApiModelProperty(value = "考勤点地址")
    private String pointAddress;


    /**
     * 经度
     */
    @ApiModelProperty(value = "经度")
    private Double lon;


    /**
     * 纬度
     */
    @ApiModelProperty(value = "纬度")
    private Double lat;


    /**
     * 高度
     */
    @ApiModelProperty(value = "高度")
    private Double alt;


    /**
     * 坐标
     */
    @ApiModelProperty(value = "坐标")
    private String gisArea;


    /**
     * 坐标系代码
     */
    @ApiModelProperty(value = "坐标系代码")
    private String gisType;

}
