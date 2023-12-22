package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectFeeConf;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 房屋费用关联表视图
 *
 * @author xull@aurine
 * @date 2020-07-29 13:27:59
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "房屋费用关联表视图")
public class ProjectHouseFeeItemConfVo extends ProjectFeeConf {
    @ApiModelProperty("房屋id")
    private String houseId;
    @ApiModelProperty("房屋费用关联id")
    private String recordId;
    @ApiModelProperty("房屋面积")
    private BigDecimal houseArea;
    @ApiModelProperty("入住时间")
    private LocalDate checkInTime;
    @ApiModelProperty("最后账单日期")
    private String lastMonth;
    @ApiModelProperty("开始记账单月")
    private String beginBillMonth;
    @ApiModelProperty("最后预付日期")
    private LocalDate lastPreDate;
    @ApiModelProperty("付款人")
    private String paidBy;
    @ApiModelProperty("付款单号")
    private String payOrderNo;
    @ApiModelProperty("实付金额")
    private BigDecimal actAmount;
    @ApiModelProperty("付款时间")
    private LocalDateTime payTime;
    @ApiModelProperty("预付单价")
    private BigDecimal preUnitPrice;
    @ApiModelProperty(value = "预付是否定额收费 1 是 0 否")
    private String preFixAmountOrNot;
    @ApiModelProperty(value = "预付定额费用")
    private BigDecimal preFixAmount;
    @ApiModelProperty("预付单位")
    private String preUnit;
    @ApiModelProperty("预付时候的房屋面积")
    private BigDecimal preHouseArea;
    @ApiModelProperty("计费类型")
    private String unitString;
}
