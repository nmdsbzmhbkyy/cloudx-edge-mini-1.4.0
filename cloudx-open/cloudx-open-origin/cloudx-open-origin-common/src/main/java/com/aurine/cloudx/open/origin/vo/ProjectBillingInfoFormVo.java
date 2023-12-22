package com.aurine.cloudx.open.origin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * (ProjectBillingInfoFormVo) 账单查询vo
 *
 * @author xull
 * @since 2020/7/23 10:16
 */
@Data
@ApiModel(value = "账单查询vo")
public class ProjectBillingInfoFormVo {
    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    private String beginDateString;
    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    private String endDateString;



    /**
     * 是否预存
     */
    @ApiModelProperty("是否预存")
    private String prestore;

    /**
     * 缴费状态
     */
    @ApiModelProperty("缴费状态")
    private String payStatus;

    /**
     * 费用名称
     */
    @ApiModelProperty("费用名称")
    private String feeName;

    /**
     * 房屋Id
     */
    @ApiModelProperty("房屋Id")
    private String houseId;

    @ApiModelProperty("交易账单id")
    private String payOrderNo;



    /**
     * 缴费时间(app端查询条件)
     */
    @ApiModelProperty("缴费时间 例如: 202008 2020年八月")
    private String payDateString;

    /**
     * 缴费状态(app端查询条件)
     */
    @ApiModelProperty("缴费类型")
    private String feeType;


    /**
     * 账单id
     */
    @ApiModelProperty("账单id")
    private String billingNo;

    /**
     * 输入框搜索条件
     */
    @ApiModelProperty("输入框搜索条件")
    private String textString;

}
