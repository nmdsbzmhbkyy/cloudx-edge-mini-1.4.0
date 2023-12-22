package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 投诉沟通历史记录
 *
 * @author guhl@aurine.cn
 * @date 2020-07-20 13:39:06
 */
@Data
@TableName("project_complaint_communicate_his")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "投诉沟通历史记录")
public class ProjectComplaintCommunicateHis extends Model<ProjectComplaintCommunicateHis> {
private static final long serialVersionUID = 1L;

    /**
     * 记录id，uuid
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value="记录id，uuid")
    private String recordId;
    /**
     * 投诉单号，关联投诉单
     */
    @ApiModelProperty(value="投诉单号，关联投诉单")
    private String complaintId;
    /**
     * 发送人
     */
    @ApiModelProperty(value="发送人")
    private String sender;
    /**
     * 接收人
     */
    @ApiModelProperty(value="接收人")
    private String receiver;
    /**
     * 内容
     */
    @ApiModelProperty(value="内容")
    private String content;
    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value="创建人")
    private Integer operator;
    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value="更新时间")
    private LocalDateTime updateTime;
    }
