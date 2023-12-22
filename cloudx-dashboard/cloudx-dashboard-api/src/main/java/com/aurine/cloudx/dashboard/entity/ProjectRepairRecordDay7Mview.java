package com.aurine.cloudx.dashboard.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 报修统计-7日
 *
 * @ClassName: ProjectRepairRecordDay7Mview
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-30 14:37
 * @Copyright:
 */
@Data
@TableName("PROJECT_REPAIR_RECORD_DAY7_MVIEW")
public class ProjectRepairRecordDay7Mview extends BaseDashboardEntity {


    /**
     * 1日
     */
    @ApiModelProperty("1日")
    @TableField("CNT_DAY01")
    private Long day01;
    /**
     * 2日
     */
    @ApiModelProperty("2日")
    @TableField("CNT_DAY02")
    private Long day02;
    /**
     * 3日
     */
    @ApiModelProperty("3日")
    @TableField("CNT_DAY03")
    private Long day03;
    /**
     * 4日
     */
    @ApiModelProperty("4日")
    @TableField("CNT_DAY04")
    private Long day04;
    /**
     * 5日
     */
    @ApiModelProperty("5日")
    @TableField("CNT_DAY05")
    private Long day05;
    /**
     * 6日
     */
    @ApiModelProperty("6日")
    @TableField("CNT_DAY06")
    private Long day06;
    /**
     * 7日
     */
    @ApiModelProperty("7日")
    @TableField("CNT_DAY07")
    private Long day07;

}
