package com.aurine.cloudx.dashboard.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 项目楼栋人员通行7小时
 *
 * @ClassName: ProjectBuildingPersonEntranceH07MView
 * @author : Qiu <qiujb@miligc.com>
 * @date : 2021 08 16 15:02
 * @Copyright:
 */

@Data
@TableName("PROJECT_BUILDING_PERSON_ENTRANCE_H07_MVIEW")
public class ProjectBuildingPersonEntranceH07MView extends BaseDashboardEntity {

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
     * 入总数
     */
    @ApiModelProperty("入总数")
    @TableField("CNT_IN_TOTAL")
    private Long cntInTotal;

    /**
     * 出总数
     */
    @ApiModelProperty("出总数")
    @TableField("CNT_OUT_TOTAL")
    private Long cntOutTotal;

    /**
     * 1小时
     */
    @ApiModelProperty("1小时")
    @TableField("CNT_H01")
    private Long cntH01;

    /**
     * 1小时入
     */
    @ApiModelProperty("1小时入")
    @TableField("CNT_IN_H01")
    private Long cntInH01;

    /**
     * 1小时出
     */
    @ApiModelProperty("1小时出")
    @TableField("CNT_OUT_H01")
    private Long cntOutH01;

    /**
     * 2小时
     */
    @ApiModelProperty("2小时")
    @TableField("CNT_H02")
    private Long cntH02;

    /**
     * 2小时入
     */
    @ApiModelProperty("2小时入")
    @TableField("CNT_IN_H02")
    private Long cntInH02;

    /**
     * 2小时出
     */
    @ApiModelProperty("2小时出")
    @TableField("CNT_OUT_H02")
    private Long cntOutH02;

    /**
     * 3小时
     */
    @ApiModelProperty("3小时")
    @TableField("CNT_H03")
    private Long cntH03;

    /**
     * 3小时入
     */
    @ApiModelProperty("3小时入")
    @TableField("CNT_IN_H03")
    private Long cntInH03;

    /**
     * 3小时出
     */
    @ApiModelProperty("3小时出")
    @TableField("CNT_OUT_H03")
    private Long cntOutH03;

    /**
     * 4小时
     */
    @ApiModelProperty("4小时")
    @TableField("CNT_H04")
    private Long cntH04;

    /**
     * 4小时入
     */
    @ApiModelProperty("4小时入")
    @TableField("CNT_IN_H04")
    private Long cntInH04;

    /**
     * 4小时出
     */
    @ApiModelProperty("4小时出")
    @TableField("CNT_OUT_H04")
    private Long cntOutH04;

    /**
     * 5小时
     */
    @ApiModelProperty("5小时")
    @TableField("CNT_H05")
    private Long cntH05;

    /**
     * 5小时入
     */
    @ApiModelProperty("5小时入")
    @TableField("CNT_IN_H05")
    private Long cntInH05;

    /**
     * 5小时出
     */
    @ApiModelProperty("5小时出")
    @TableField("CNT_OUT_H05")
    private Long cntOutH05;

    /**
     * 6小时
     */
    @ApiModelProperty("6小时")
    @TableField("CNT_H06")
    private Long cntH06;

    /**
     * 6小时入
     */
    @ApiModelProperty("6小时入")
    @TableField("CNT_IN_H06")
    private Long cntInH06;

    /**
     * 6小时出
     */
    @ApiModelProperty("6小时出")
    @TableField("CNT_OUT_H06")
    private Long cntOutH06;

    /**
     * 7小时
     */
    @ApiModelProperty("7小时")
    @TableField("CNT_H07")
    private Long cntH07;

    /**
     * 7小时入
     */
    @ApiModelProperty("7小时入")
    @TableField("CNT_IN_H07")
    private Long cntInH07;

    /**
     * 7小时出
     */
    @ApiModelProperty("7小时出")
    @TableField("CNT_OUT_H07")
    private Long cntOutH07;
}
