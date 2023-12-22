package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 推送中心消息模板配置(ProjectNoticeTemplate)表实体类
 *
 * @author makejava
 * @since 2020-12-14 09:34:11
 */
@Data
@TableName("project_notice_template")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "推送中心消息模板配置(ProjectNoticeTemplate)")
public class ProjectNoticeTemplate extends Model<ProjectNoticeTemplate> {

    private static final long serialVersionUID = -98936190796738162L;


    /**
     * 模板id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "模板id")
    private String templateId;


    /**
     * 模板类型id，关联project_notice_category_conf.typeId
     */
    @ApiModelProperty(value = "模板类型id")
    private String typeId;


    /**
     * 模板标题
     */
    @ApiModelProperty(value = "模板标题")
    private String title;


    /**
     * 模板内容
     */
    @ApiModelProperty(value = "模板内容")
    private String content;


    /**
     * 是否启用
     */
    @ApiModelProperty(value = "是否启用")
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
    @ApiModelProperty(value = "更新人")
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
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}