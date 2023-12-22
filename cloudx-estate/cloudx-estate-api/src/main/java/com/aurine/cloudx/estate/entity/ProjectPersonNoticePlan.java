package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 住户通知计划(ProjectPersonNoticePlan)表实体类
 *
 * @author makejava
 * @since 2020-12-14 10:08:21
 */
@Data
@TableName("project_person_notice_plan")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "住户通知计划(ProjectPersonNoticePlan)")
public class ProjectPersonNoticePlan extends Model<ProjectPersonNoticePlan> {

    private static final long serialVersionUID = -45353569374871323L;


    /**
     * 计划id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "计划id")
    private String planId;


    /**
     * 计划名称
     */
    @ApiModelProperty(value = "计划名称")
    private String planName;


    /**
     * 频率
     */
    @ApiModelProperty(value = "频率")
    private String frequency;


    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private LocalDateTime beginTime;


    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endTime;


    /**
     * 文本标题
     */
    @ApiModelProperty(value = "文本标题")
    private String title;


    /**
     * 发送对象
     */
    @ApiModelProperty(value = "发送对象")
    private String targetType;


    /**
     * 消息类型
     */
    @ApiModelProperty(value = "消息类型")
    private String noticeCatetory;

    /**
     * 模板id
     */
    @ApiModelProperty(value = "模板id")
    private String templateId;


    @ApiModelProperty(value = "文本类型")
    private String contentType;
    /**
     * 内容
     */
    @ApiModelProperty(value = "内容")
    private String content;




    /**
     * 是否启用 1 启用 0 禁用
     */
    @ApiModelProperty(value = "是否启用 1 启用 0 禁用")
    private String isActive;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private Integer creator;


    /**
     * 最后更新人
     */
    @ApiModelProperty(value = "最后更新人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
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
    //@TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;



}