package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 项目已出账的账单信息(ProjectBillingInfo)表实体类
 *
 * @author makejava
 * @since 2020-07-20 16:43:48
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "项目已出账的账单信息(ProjectBillingInfo)")
public class ProjectBillingInfo extends Model<ProjectBillingInfo> {

    private static final long serialVersionUID = -47606471981715140L;


    /**
     * 账单id，uuid
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "账单id，uuid")
    private String billingNo;

    /**
     * 费用名称
     */
    @ApiModelProperty("费用名称")
    private String feeName;
    /**
     * 是否预存
     */
    @ApiModelProperty("是否预存")
    @TableField("isPrestore")
    private String prestore;
    /**
     * 账单月
     */
    @ApiModelProperty(value = "账单月")
    private String billMonth;

    /**
     * 房屋id
     */
    @ApiModelProperty(value = "房屋id")
    private String houseId;

    /**
     * 房屋面积
     */
    @ApiModelProperty("房屋面积")
    private BigDecimal houseArea;

    /**
     * 费用id
     */
    @ApiModelProperty(value = "费用id")
    private String feeId;

    /**
     * 计费周期开始时间
     */
    @ApiModelProperty(value = "计费周期开始时间")
    private LocalDate feeCycleStart;

    /**
     * 计费周期结束时间
     */
    @ApiModelProperty(value = "计费周期结束时间")
    private LocalDate feeCycleEnd;

    /**
     * 支付人
     */
    @ApiModelProperty(value = "支付人")
    private String paidBy;

    /**
     * 付款时间
     */
    @ApiModelProperty(value = "付款时间")
    private LocalDateTime payTime;

    /**
     * 应付金额
     */
    @ApiModelProperty(value = "应付金额")
    private BigDecimal payAbleAmount;

    /**
     * 优惠金额
     */
    @ApiModelProperty(value = "优惠金额")
    private BigDecimal promotionAmount;

    /**
     * 实付金额
     */
    @ApiModelProperty(value = "实付金额")
    private BigDecimal actAmount;

    /**
     * 订单状态
     */
    @ApiModelProperty(value = "订单状态")
    private String payStatus;
    /**
     * 订单单号
     */
    @ApiModelProperty(value = "订单单号")
    private String payOrderNO;



    /**
     * dcTimes
     */
    @ApiModelProperty(value = "催缴次数")
    private Integer dcTimes;


    /**
     * 是否定额收费 1 是 0 否
     */
    @ApiModelProperty(value = "是否定额收费 1 是 0 否")
    @TableField("isFixAmount")
    private String fixAmountOrNot;


    /**
     * 定额费用
     */
    @ApiModelProperty(value = "定额费用")
    private BigDecimal fixAmount;

    /**
     * 单位
     */
    @ApiModelProperty("单位")
    private String unit;
    /**
     * 单价
     */
    @ApiModelProperty("单价")
    private BigDecimal unitPrice;
    /**
     * 项目id
     */
    @ApiModelProperty("项目id")
    private Integer projectId ;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private Integer operator;


    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;


}