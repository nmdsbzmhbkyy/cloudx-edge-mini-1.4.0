package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectPromotionConf;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * (ProjectPromotionConfVo) 优惠费用配置视图
 *
 * @author xull
 * @since 2020/7/6 10:16
 */
@Data
public class ProjectPromotionConfPageVo extends ProjectPromotionConf {
    @ApiModelProperty("是否有效")
    private String valid;
    @ApiModelProperty("创建人名称")
    private String operatorName;
}
