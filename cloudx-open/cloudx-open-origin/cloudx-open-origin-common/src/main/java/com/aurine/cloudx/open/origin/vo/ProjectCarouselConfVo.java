package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectCarouselConf;
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
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "轮播图资讯管理(ProjectCarouselConf)")
public class ProjectCarouselConfVo extends ProjectCarouselConf {

   /**
    * 创建者姓名
    * */
   @ApiModelProperty("创建者姓名")
   String operateName;

   /**
    * 创建时间
    * */
   @ApiModelProperty("创建时间")
   String createTimeStr;

}