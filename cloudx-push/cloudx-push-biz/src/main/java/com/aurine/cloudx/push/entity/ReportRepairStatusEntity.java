package com.aurine.cloudx.push.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 报修处理进展
 *
 * @ClassName: ReportRepairStatusEntity
 * @author: 邹宇
 * @date: 2021-8-27 14:06:49
 * @Copyright:
 */
@Data
@ApiModel(value = "报修处理进展")
public class ReportRepairStatusEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 是否完成
     * true 完成
     * false 变更
     */
    @ApiModelProperty(value = "是否完成", required = true)
    private boolean isItDone;


    /**
     * 报修位置
     */
    @ApiModelProperty(value = "报修位置", required = true)
    private String reportRepairLocation;

    /**
     * 报修主题
     */
    @ApiModelProperty(value = "报修主题", required = true)
    private String reportRepairTheme;

    /**
     * 报修时间
     */
    @ApiModelProperty(value = "报修时间", required = true)
    private String reportRepairTime;


    /**
     * 处理进展
     */
    @ApiModelProperty(value = "处理进展", required = true)
    private String currentProgress;

    /**
     * 预计完成日期
     */
    @ApiModelProperty(value = "预计完成日期")
    private String estimatedCompletionDate;

}
