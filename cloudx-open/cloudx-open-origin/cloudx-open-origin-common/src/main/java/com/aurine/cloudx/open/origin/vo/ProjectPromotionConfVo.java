package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectPromotionConf;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * (ProjectPromotionConfVo) 优惠费用配置视图
 *
 * @author xull
 * @since 2020/7/6 10:16
 */
@Data
public class ProjectPromotionConfVo extends ProjectPromotionConf {
    /**
     * 费用id列表
     */
    @ApiModelProperty("费用id列表")
    List<String> feeIds;
    /**
     * 是否全选
     */
    @ApiModelProperty("是否全选 1是 0 否")
    String selectType;
}
