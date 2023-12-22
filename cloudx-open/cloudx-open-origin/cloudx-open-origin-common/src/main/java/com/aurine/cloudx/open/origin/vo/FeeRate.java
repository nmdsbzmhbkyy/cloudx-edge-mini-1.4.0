package com.aurine.cloudx.open.origin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


/**
 * @author cjw
 * @description: 关键指标
 * @date 2021/7/8 14:44
 */
@Data
@ApiModel(value = "关键指标vo")
public class FeeRate  {
    /**
     * 是否存在上月以前未缴账单
     */
    @ApiModelProperty("应收金额")
    private BigDecimal payableAmount;
    /**
     * 是否存在未缴账单
     */
    @ApiModelProperty("已收金额")
    private BigDecimal actAmount;
    /**
     * 最后一次缴费月份(或最后一个账单月份)
     */
    @ApiModelProperty("未收金额")
    private String lastMonth;

    @ApiModelProperty("减免金额")
    private BigDecimal promotionAmount;

    @ApiModelProperty("未收金额")
    private BigDecimal unPay;

    @ApiModelProperty("收缴率")
    private String feeRate;




}
