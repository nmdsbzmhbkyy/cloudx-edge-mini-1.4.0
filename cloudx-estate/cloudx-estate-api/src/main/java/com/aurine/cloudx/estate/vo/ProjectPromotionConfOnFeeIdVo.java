package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectPromotionConf;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 优惠信息关联费用(ProjectPromotionConfFormVo)视图表
 *
 * @author xull@aurine.cn
 * @date 2020-07-24 09:52:28
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("优惠信息关联费用视图")
public class ProjectPromotionConfOnFeeIdVo extends ProjectPromotionConf {
    private String feeId;
}
