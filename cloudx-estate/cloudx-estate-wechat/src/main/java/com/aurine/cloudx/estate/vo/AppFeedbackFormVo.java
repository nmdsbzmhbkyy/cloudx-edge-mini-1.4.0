package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("意见反馈登记表单")
public class AppFeedbackFormVo {

    /**
     * 联系方式
     */
    @ApiModelProperty(value = "联系方式", required = true)
    private String contactInfo;

    /**
     * 意见内容
     */
    @ApiModelProperty(value = "意见内容", required = true)
    private String content;

    /**
     * 系统来源  1 物业小程序 2 业主小程序 3 业主app
     */
    @ApiModelProperty(value = "系统来源  1 物业小程序 2 业主小程序 3 业主app", required = true)
    private String origin;
}
