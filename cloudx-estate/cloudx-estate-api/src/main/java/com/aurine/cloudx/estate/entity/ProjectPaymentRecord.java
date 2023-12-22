package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 交易记录(ProjectPaymentRecord)表实体类
 *
 * @author makejava
 * @since 2020-07-23 18:54:07
 */
@Data
@TableName("project_payment_record")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "交易记录(ProjectPaymentRecord)")
public class ProjectPaymentRecord extends Model<ProjectPaymentRecord> {

    private static final long serialVersionUID = -44252101955053642L;


    /**
     * 缴费单号，uuid
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "缴费单号，uuid")
    private String payOrderNo;


    /**
     * 房号
     */
    @ApiModelProperty(value = "房号")
    private String houseId;


    /**
     * 房屋名称
     */
    @ApiModelProperty(value = "房屋名称")
    private String houseName;

    /**
     * 房号
     */
    @ApiModelProperty(value = "单元id")
    private String unitId;


    /**
     * 房屋名称
     */
    @ApiModelProperty(value = "单元id")
    private String unitName;


    /**
     * 房号
     */
    @ApiModelProperty(value = "楼栋id")
    private String buildingId;


    /**
     * 房屋名称
     */
    @ApiModelProperty(value = "楼栋名称")
    private String buildingName;


    /**
     * 支付人
     */
    @ApiModelProperty(value = "支付人")
    private String paidBy;


    /**
     * 支付人
     */
    @ApiModelProperty(value = "支付人账号")
    private String payAccount;

    /**
     * 应付总额
     */
    @ApiModelProperty(value = "应付总额")
    private BigDecimal payableAmount;


    /**
     * 优惠总额
     */
    @ApiModelProperty(value = "优惠总额")
    private BigDecimal promotionAmount;


    /**
     * 实付总额
     */
    @ApiModelProperty(value = "实付总额")
    private BigDecimal actAmount;


    /**
     * 支付方式
     */
    @ApiModelProperty(value = "支付方式")
    private String payType;


    /**
     * 付款时间
     */
    @ApiModelProperty(value = "付款时间")
    private LocalDateTime payTime;


    /**
     * 订单状态
     */
    @ApiModelProperty(value = "订单状态")
    private String orderStatus;

    @ApiModelProperty(value = "订单号")
    private String billingNo;

    @ApiModelProperty(value = "收款账号id")

    private String accountId;

    @ApiModelProperty(value = "余额")
    private BigDecimal balance;
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

    /**
     * 项目id
     */
    @ApiModelProperty("项目id")
    private Integer projectId ;

}