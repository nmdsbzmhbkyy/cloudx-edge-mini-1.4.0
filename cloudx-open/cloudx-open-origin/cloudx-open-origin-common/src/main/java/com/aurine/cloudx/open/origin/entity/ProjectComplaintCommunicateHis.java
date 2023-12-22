package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
public class ProjectComplaintCommunicateHis extends OpenBasePo<ProjectComplaintCommunicateHis> {
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
    }
