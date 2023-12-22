package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 房屋费用汇总视图
 *
 * @author xull@aurine
 * @date 2020-07-29 13:27:59
 */
@Data
@ApiModel("房屋费用汇总Vo")
public class ProjectHouseFeeTotalVo {
    /**
     * 楼栋编号
     */
    @ApiModelProperty(value = "楼栋编号")
    private String buildingId;

    /**
     * 楼栋名称
     */
    @ApiModelProperty(value = "楼栋名称")
    private String buildingName;

    /**
     * 单元编号
     */
    @ApiModelProperty(value = "单元编号")
    private String unitId;

    /**
     * 单元名称
     */
    @ApiModelProperty(value = "单元名称")
    private String unitName;
    /**
     * 房屋id
     */
    @ApiModelProperty("房屋id")
    private String houseId;
    /**
     * 房屋名称
     */
    @ApiModelProperty("房屋名称")
    private String houseName;
    /**
     * 未收金额
     */
    @ApiModelProperty("未收金额")
    private BigDecimal uncollected;
    /**
     * 未缴清单数量
     */
    @ApiModelProperty("未缴清单数量")
    private BigDecimal uncollectedNum;
    /**
     * 应收总金额
     */
    @ApiModelProperty("应收总金额")
    private BigDecimal totalPayableAmount;
    /**
     * 实收总金额
     */
    @ApiModelProperty("实收总金额")
    private BigDecimal totalActAmount;
    /**
     * 优惠总金额
     */
    @ApiModelProperty("优惠总金额")
    private BigDecimal totalPromotionAmount;
    /**
     * 已退款金额
     */
    @ApiModelProperty("已退款金额")
    private BigDecimal totalRefundAmount;

    /**
     * 入住日期
     */
    @ApiModelProperty("入住日期")
    private LocalDateTime checkInTime;

    /**
     * 是否设置收费
     */
    @ApiModelProperty("是否设置收费")
    private String haveSetFee;

    /**
     * 是否缴清
     */
    @ApiModelProperty("是否缴清")
    private String havePayUp;



    @ApiModelProperty("生成X月账单")
    private Integer genMonth;


    @ApiModelProperty("房屋面积")
    private BigDecimal houseArea;
}
