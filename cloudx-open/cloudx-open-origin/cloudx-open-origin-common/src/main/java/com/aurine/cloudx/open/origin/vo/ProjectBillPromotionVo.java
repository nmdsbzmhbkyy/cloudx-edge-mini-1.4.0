package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectPromotionConf;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * (ProjectBillPromotionVo) 优惠账单vo
 *
 * @author xull
 * @since 2020/7/24 10:16
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("优惠账单查询vo")
public class ProjectBillPromotionVo extends ProjectBillingInfoVo {



    /**
     * 预存优惠列表
     */
    @ApiModelProperty("预存优惠列表")
    private List<ProjectPromotionConf> prePromotionList;

}
