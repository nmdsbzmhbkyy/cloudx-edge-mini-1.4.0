package com.aurine.cloudx.dashboard.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 报修记录
 * @ClassName: ProjectComplaintRecordMview
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-29 14:37
 * @Copyright:
 */
@Data
@TableName("PROJECT_REPAIR_RECORD_MVIEW")
public class ProjectRepairRecordMview extends BaseDashboardEntity {


    /**
     * 1月
     */
    @ApiModelProperty("1月")
    @TableField("CNT_MONTH01")
    private Long month01;
    /**
     * 2月
     */
    @ApiModelProperty("2月")
    @TableField("CNT_MONTH02")
    private Long month02;
    /**
     * 3月
     */
    @ApiModelProperty("3月")
    @TableField("CNT_MONTH03")
    private Long month03;
    /**
     * 4月
     */
    @ApiModelProperty("4月")
    @TableField("CNT_MONTH04")
    private Long month04;
    /**
     * 5月
     */
    @ApiModelProperty("5月")
    @TableField("CNT_MONTH05")
    private Long month05;
    /**
     * 6月
     */
    @ApiModelProperty("6月")
    @TableField("CNT_MONTH06")
    private Long month06;
    /**
     * 7月
     */
    @ApiModelProperty("7月")
    @TableField("CNT_MONTH07")
    private Long month07;
    /**
     * 8月
     */
    @ApiModelProperty("8月")
    @TableField("CNT_MONTH08")
    private Long month08;
    /**
     * 9月
     */
    @ApiModelProperty("9月")
    @TableField("CNT_MONTH09")
    private Long month09;
    /**
     * 10月
     */
    @ApiModelProperty("10月")
    @TableField("CNT_MONTH10")
    private Long month10;
    /**
     * 11月
     */
    @ApiModelProperty("11月")
    @TableField("CNT_MONTH11")
    private Long month11;
    /**
     * 12月
     */
    @ApiModelProperty("12月")
    @TableField("CNT_MONTH12")
    private Long month12;


}
