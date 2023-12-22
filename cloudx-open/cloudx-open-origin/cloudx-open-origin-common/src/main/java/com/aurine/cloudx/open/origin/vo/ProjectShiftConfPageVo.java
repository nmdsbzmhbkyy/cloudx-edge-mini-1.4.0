package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectShiftConf;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * Title: ProjectShiftConfPageVo
 * Description: 班次设置查询Vo
 *
 * @author guhl@aurine.cn
 * @version 1.0.0
 * @date 2020/7/20 14:49
 */
@Data
@ApiModel(value = "班次设置分页查询视图")
public class ProjectShiftConfPageVo extends ProjectShiftConf {
    /**
     * 查询时间范围:开始时间
     * <p>
     * 字符串类型 用于接收前端传值
     */
    @ApiModelProperty("查询时间范围:开始时间")
    private String startTimeString;
    /**
     * 查询时间范围:结束时间
     */
    @ApiModelProperty("查询时间范围:结束时间")
    private String endTimeString;
    /**
     * 查询时间范围:开始时间
     */
    @ApiModelProperty("查询时间范围:开始时间")
    private LocalDate startTime;
    /**
     * 查询时间范围:结束时间
     */
    @ApiModelProperty("查询时间范围:结束时间")
    private LocalDate endTime;
    /**
     * 设置人
     */
    @ApiModelProperty("设置人")
    private String operatorName;
}
