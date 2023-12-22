package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectPersonNoticePlan;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * (ProjectPersonNoticePlanPageVo)
 *
 * @author guhl
 * @version 1.0.0
 * @date 2020/12/15 15:51
 */
@Data
@ApiModel("住户通知计划分页vo")
public class ProjectPersonNoticePlanPageVo extends ProjectPersonNoticePlan {

    /**
     * 创建人姓名
     */
    @ApiModelProperty("创建人姓名")
    public String creatorName;

    /**
     * 创建时间字符串
     */
    @ApiModelProperty("创建起始时间字符串")
    public String createStartTimeString;

    /**
     * 创建时间字符串
     */
    @ApiModelProperty("创建终止时间字符串")
    public String createEndTimeString;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建终止时间")
    public LocalDateTime createStartTime;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建终止时间")
    public LocalDateTime createEndTime;
}
