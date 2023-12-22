package com.aurine.cloudx.dashboard.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 项目楼栋告警事件7日
 *
 * @ClassName: ProjectBuildingHouseTotalMView
 * @author : Qiu <qiujb@miligc.com>
 * @date : 2021 08 17 10:51
 * @Copyright:
 */

@Data
@TableName("PROJECT_BUILDING_ALARM_D07_MVIEW")
public class ProjectBuildingAlarmD07MView extends BaseDashboardEntity {

    /**
     * 楼栋ID
     */
    @ApiModelProperty("楼栋ID")
    @TableField("BUILDINGID")
    private String buildingId;

    /**
     * 楼栋名称
     */
    @ApiModelProperty("楼栋名称")
    @TableField("BUILDINGNAME")
    private String buildingName;

    /**
     * 楼层
     */
    @ApiModelProperty("楼层")
    @TableField("STOREY")
    private Integer storey;

    /**
     * 总数
     */
    @ApiModelProperty("总数")
    @TableField("CNT_TOTAL")
    private Long cntTotal;

    /**
     * 未处理
     */
    @ApiModelProperty("未处理")
    @TableField("CNT_NOT_PROCESS")
    private Long cntNotProcess;

    /**
     * 处理中
     */
    @ApiModelProperty("处理中")
    @TableField("CNT_IN_PROCESS")
    private Long cntInProcess;

    /**
     * 已处理
     */
    @ApiModelProperty("已处理")
    @TableField("CNT_PROCESSED")
    private Long cntProcessed;

    /**
     * 1日
     */
    @ApiModelProperty("1日")
    @TableField("CNT_D01")
    private Long cntD01;

    /**
     * 2日
     */
    @ApiModelProperty("2日")
    @TableField("CNT_D02")
    private Long cntD02;

    /**
     * 3日
     */
    @ApiModelProperty("3日")
    @TableField("CNT_D03")
    private Long cntD03;

    /**
     * 4日
     */
    @ApiModelProperty("4日")
    @TableField("CNT_D04")
    private Long cntD04;

    /**
     * 5日
     */
    @ApiModelProperty("5日")
    @TableField("CNT_D05")
    private Long cntD05;

    /**
     * 6日
     */
    @ApiModelProperty("6日")
    @TableField("CNT_D06")
    private Long cntD06;

    /**
     * 7日
     */
    @ApiModelProperty("7日")
    @TableField("CNT_D07")
    private Long cntD07;
}
