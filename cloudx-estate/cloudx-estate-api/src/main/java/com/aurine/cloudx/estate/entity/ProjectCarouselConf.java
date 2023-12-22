package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

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
public class ProjectCarouselConf extends Model<ProjectCarouselConf> {

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
     * 最后操作人
     */
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "最后操作人")
    private Integer operator;


}