package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectBillingInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * (ProjectBillingInfoFormVo) 账单信息vo
 *
 * @author xull
 * @since 2020/7/24 15:16
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "账单信息vo")
public class ProjectBillingInfoVo extends ProjectBillingInfo {
    /**
     * 是否存在上月以前未缴账单
     */
    @ApiModelProperty("是否存在上月以前未缴账单")
    private String lastHave;
    /**
     * 是否存在未缴账单
     */
    @ApiModelProperty("是否存在未缴账单")
    private String allHave;
    /**
     * 最后一次缴费月份(或最后一个账单月份)
     */
    @ApiModelProperty("最后一次缴费月份")
    private String lastMonth;

    @ApiModelProperty("计费标准")
    private String unitString;


    @ApiModelProperty("费用类型name")
    private String feeLabel;


    @ApiModelProperty("房屋名")
    private String houseName;


    @ApiModelProperty("房屋业主id")
    private String personId;

    @ApiModelProperty("费用类型value")
    private String feeType;


    @ApiModelProperty(value = "查询范围")
    private String[] dateRange;

    @ApiModelProperty(value = "月份差值")
    private Integer monthValue;

    @ApiModelProperty(value = "欠费总金额")
    private  Double theTotalAmountOfArrears;

}
