package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 平台意见反馈(SysPlatFeedback)表实体类
 *
 * @author xull
 * @since 2021-03-05 09:58:54
 */
@Data
@TableName("sys_plat_feedback")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "平台意见反馈(SysPlatFeedback)")
public class SysPlatFeedback extends Model<SysPlatFeedback> {

    private static final long serialVersionUID = -34984667674575630L;


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
    @ApiModelProperty(value = "系统来源  1 物业小程序 2 业主小程序 3 业主app 4 物业app")
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

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private Integer operator;


    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;


}
