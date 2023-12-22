package com.aurine.cloudx.open.origin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 轮播图资讯管理(ProjectCarouselConf)表实体类
 *
 * @author 王良俊
 * @since 2021-01-12 11:43:01
 */
@Data
@ApiModel(value = "轮播图资讯管理(ProjectCarouselConf)")
public class ProjectCarouselConfQuery{


    /**
     * 标题
     */
    @ApiModelProperty("标题")
    private String title;

    /**
     * 发布时间范围
     * */
    @ApiModelProperty("发布时间范围")
    private LocalDateTime[] timeRange;

}