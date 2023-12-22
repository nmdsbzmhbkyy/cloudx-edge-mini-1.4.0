package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectBillingInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * (ProjectBillingInfoFormVo) 账单信息vo
 *
 * @author xull
 * @since 2020/7/24 15:16
 */
@Data
@ApiModel(value = "账单信息vo")
public class AppProjectBillingInfoVo extends ProjectBillingInfo {


    @ApiModelProperty("前端展示月份")
    private  String billMonthName;
    @ApiModelProperty("前端展示费用名")
    private  String feeNameString;
    @ApiModelProperty("前端展示费用类型")
    private  String feeLabelString;
}
