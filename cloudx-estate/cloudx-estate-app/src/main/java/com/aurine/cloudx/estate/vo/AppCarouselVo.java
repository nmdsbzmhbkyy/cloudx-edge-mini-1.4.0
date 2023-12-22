package com.aurine.cloudx.estate.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
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
@ApiModel(value = "轮播图")
public class AppCarouselVo {

    

    /**
     * 轮播图id，uuid
     */
    @ApiModelProperty(value = "轮播图Id，uuid")
    private String carouselId;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;

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
     * 来源, 1 项目内 2 外部链接
     */
    @ApiModelProperty(value = "来源  1 项目内 2 外部链接")
    private String origin;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private LocalDateTime expTime;

    
}
