package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectCarousel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 轮播图管理Vo
 *
 * @author DW
 * @version 1.0.0
 * @date 2021/1/12 16:22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "轮播图管理Vo")
public class ProjectCarouselVo extends ProjectCarousel {

    /**
     * 创建人名称
     */
    @ApiModelProperty(value = "创建人名称")
    private String createName;
    
}
