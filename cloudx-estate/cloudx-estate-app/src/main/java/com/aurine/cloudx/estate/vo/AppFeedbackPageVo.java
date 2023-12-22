package com.aurine.cloudx.estate.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel("意见反馈列表")
public class AppFeedbackPageVo {
    /**
     * 意见id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "意见id")
    private String feedbackId;


    /**
     * 用户id(该字段需要关联员工表业主表)
     */
    @ApiModelProperty(value = "用户id")
    private String userId;


    /**
     * 系统来源  1 物业小程序 2 业主小程序 3 业主app
     */
    @ApiModelProperty(value = "系统来源  1 物业小程序 2 业主小程序 3 业主app")
    private String origin;


    /**
     * 意见内容
     */
    @ApiModelProperty(value = "意见内容")
    private String content;


    /**
     * 意见时间
     */
    @ApiModelProperty(value = "意见时间")
    private LocalDateTime feedbackTime;


    /**
     * 状态 0 未回复 1 已回复
     */
    @ApiModelProperty(value = "状态 0 未回复 1 已回复")
    private String status;


    /**
     * 回复内容
     */
    @ApiModelProperty(value = "回复内容")
    private String replyContent;


    /**
     * 回复时间
     */
    @ApiModelProperty(value = "回复时间")
    private LocalDateTime replyTime;

    /**
     * 联系方式
     */
    @ApiModelProperty(value = "联系方式")
    private String contactInfo;

}
