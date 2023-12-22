package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 轮播图资讯管理(ProjectCarouselConf)表实体类
 *
 * @author 王良俊
 * @since 2021-01-12 11:43:01
 */
@Data
@TableName("project_carousel_conf")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "轮播图资讯管理(ProjectCarouselConf)")
public class ProjectCarouselConf extends OpenBasePo<ProjectCarouselConf> {

    private static final long serialVersionUID = -46209476498301907L;

    /**
     * 资讯id,uuid
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "资讯id,uuid")
    private String infoId;


    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String title;


    /**
     * 封面图
     */
    @ApiModelProperty(value = "封面图")
    private String coverPic;


    /**
     * 来源 1 自编辑 2 外部链接
     */
    @ApiModelProperty(value = "来源 1 自编辑 2 外部链接")
    private String origin;


    /**
     * 内容（自编辑）
     */
    @ApiModelProperty(value = "内容（自编辑）")
    private String content;


    /**
     * 外部链接
     */
    @ApiModelProperty(value = "外部链接")
    private String outLink;


}