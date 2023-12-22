package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
public class ProjectNoticeCategoryConf extends OpenBasePo<ProjectNoticeCategoryConf> {

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
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private Integer creator;


}