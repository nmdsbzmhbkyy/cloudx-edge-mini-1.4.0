package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectComplaintRecord;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * Title: ProjectComplaintRecordPageVo
 * Description: 投诉信息查询Vo
 *
 * @author guhl@aurine.cn
 * @version 1.0.0
 * @date 2020/7/20 14:49
 */
@Data
@ApiModel(value = "报事投诉分页查询视图")
public class ProjectComplaintRecordPageVo extends ProjectComplaintRecord {
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
     * 当前房屋名称
     */
    @ApiModelProperty("当前房屋名称")
    String houseName;
    /**
     * 当前单元名称
     */
    @ApiModelProperty("当前单元名称")
    String unitName;
    /**
     * 当前楼栋名称
     */
    @ApiModelProperty("当前楼栋名称")
    String buildingName;
    /**
     * 当前房屋地址
     */
    @ApiModelProperty("当前房屋地址")
    String buildingAddress;
    /**
     * 处理人姓名
     */
    @ApiModelProperty("处理人姓名")
    String staffName;
}
