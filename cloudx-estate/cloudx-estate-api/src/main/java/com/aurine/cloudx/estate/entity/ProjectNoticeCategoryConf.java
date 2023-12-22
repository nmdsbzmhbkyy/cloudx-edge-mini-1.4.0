package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 推送模板类型配置（信息分类），该类型可应用于媒体通知和推送中心(ProjectNoticeCategoryConf)表实体类
 *
 * @author makejava
 * @since 2020-12-14 10:06:39
 */
@Data
@TableName("project_notice_category_conf")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "推送模板类型配置（信息分类），该类型可应用于媒体通知和推送中心(ProjectNoticeCategoryConf)")
public class ProjectNoticeCategoryConf extends Model<ProjectNoticeCategoryConf> {

    private static final long serialVersionUID = -59768112693873616L;


    /**
     * 模板类型id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "模板类型id")
    private String typeId;


    /**
     * 模板类型名称
     */
    @ApiModelProperty(value = "模板类型名称")
    private String typeName;


    /**
     * 是否启用 1 是 0 否
     */
    @ApiModelProperty(value = "是否启用 1 是 0 否")
    private String isActive;


    /**
     * 租户id
     */
    @ApiModelProperty(value = "租户id")
    private Integer tenant_id;


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
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}