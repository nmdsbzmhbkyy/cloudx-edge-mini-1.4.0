package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectNotice;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * Title: ProjectNoticeFormVo
 * Description: 信息发布查询表单视图
 *
 * @author guhl@aurine.cn
 * @version 1.0.0
 * @date 2020/5/20 16:49
 */
@Data
@ApiModel(value = "信息发布分页查询表单")
public class ProjectNoticeFormVo extends ProjectNotice {
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
}
