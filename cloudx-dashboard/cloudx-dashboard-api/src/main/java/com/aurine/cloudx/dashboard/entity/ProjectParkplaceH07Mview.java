package com.aurine.cloudx.dashboard.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 车位使用趋势-7小时
 *
 * @ClassName: ProjectParkplaceH07Mview
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-30 14:37
 * @Copyright:
 */
@Data
@TableName("PROJECT_PARKPLACE_H07_MVIEW")
public class ProjectParkplaceH07Mview extends BaseDashboardEntity {


    /**
     * 总数
     */
    @ApiModelProperty("总数")
    @TableField("CNT_TOTAL")
    private Long total;
    /**
     * 1小时
     */
    @ApiModelProperty("1小时")
    @TableField("CNT_INUSE_H01")
    private Long h01;
    /**
     * 2小时
     */
    @ApiModelProperty("2小时")
    @TableField("CNT_INUSE_H02")
    private Long h02;
    /**
     * 3小时
     */
    @ApiModelProperty("3小时")
    @TableField("CNT_INUSE_H03")
    private Long h03;
    /**
     * 4小时
     */
    @ApiModelProperty("4小时")
    @TableField("CNT_INUSE_H04")
    private Long h04;
    /**
     * 5小时
     */
    @ApiModelProperty("5小时")
    @TableField("CNT_INUSE_H05")
    private Long h05;
    /**
     * 6小时
     */
    @ApiModelProperty("6小时")
    @TableField("CNT_INUSE_H06")
    private Long h06;
    /**
     * 7小时
     */
    @ApiModelProperty("7小时")
    @TableField("CNT_INUSE_H07")
    private Long h07;

}
