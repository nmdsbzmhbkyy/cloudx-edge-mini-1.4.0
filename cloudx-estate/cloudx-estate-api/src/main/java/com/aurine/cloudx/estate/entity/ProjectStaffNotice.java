package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 员工通知发布
 *
 * @author guhl@aurine.cn
 * @date 2020-07-06 11:16:25
 */
@Data
@TableName("project_staff_notice")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "员工通知发布")
public class ProjectStaffNotice extends Model<ProjectStaffNotice> {
    private static final long serialVersionUID = 1L;

    /**
     * 信息id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "信息id")
    private String noticeId;

    /**
     * 类型 '1' 纯文本 '2' 富文本
     */
    @ApiModelProperty(value = "类型 '1' 纯文本 '2' 富文本")
    private String contentType;
    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String noticeTitle;
    /**
     * 文本内容
     */
    @ApiModelProperty(value = "文本内容")
    private String content;
    /**
     * 发布时间
     */
    @ApiModelProperty(value = "发布时间")
    private LocalDateTime pubTime;
    /**
     * 发送对象，冗余项，用作拼接展示，超过长度用...表示
     */
    @ApiModelProperty(value = "发送对象，冗余项，用作拼接展示，超过长度用...表示")
    private String target;
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
    
    /**
     * 发送者
     */
    @ApiModelProperty(value = "发送者")
    private String sender;

    @ApiModelProperty(value = "通知来源 '1' 通知资讯 '2' 工单通知 ")
    private String source;
}
