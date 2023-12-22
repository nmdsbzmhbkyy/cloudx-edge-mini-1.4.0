package com.aurine.cloudx.open.origin.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SysPlatFeedbackFormVo {

    /**
     * 意见内容
     */
    @ApiModelProperty(value = "意见内容")
    private String content;

    /**
     * 用户账号
     */
    @ApiModelProperty(value = "用户账号")
    private String username;

    /**
     * 系统来源  1 物业小程序 2 业主小程序 3 业主app
     */
    @ApiModelProperty(value = "系统来源  1 物业小程序 2 业主小程序 3 业主app")
    private String origin;


    /**
     * 状态 0 未回复 1 已回复
     */
    @ApiModelProperty(value = "状态 0 未回复 1 已回复")
    private String status;
    /**
     * 联系方式
     */
    @ApiModelProperty(value = "联系方式")
    private String contactInfo;
}
