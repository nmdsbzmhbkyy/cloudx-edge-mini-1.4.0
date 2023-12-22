package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 轮播图管理
 *
 * @author DW
 * @version 1.0.0
 * @date 2021/1/12 13:54
 */
@Data
@TableName("project_carousel")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "轮播图管理")
public class ProjectCarousel extends OpenBasePo<ProjectCarousel> {

    private static final long serialVersionUID = 1L;


    /**
     * 轮播图id，uuid
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "轮播图Id，uuid")
    private String carouselId;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;

    /**
     * 类型, 1 业务端 2 物业端
     */
    @ApiModelProperty(value = "类型, 1 业主端 2 物业端")
    private String type;

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
     * 来源, 1 项目内 2 外部链接
     */
    @ApiModelProperty(value = "来源")
    private String origin;

    /**
     * 资讯id
     */
    @ApiModelProperty(value = "资讯id")
    private String infoId;

    /**
     * 外部链接
     */
    @ApiModelProperty(value = "外部链接")
    private String outLink;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private LocalDateTime expTime;
    
}
