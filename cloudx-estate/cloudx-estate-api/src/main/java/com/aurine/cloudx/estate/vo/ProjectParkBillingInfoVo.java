/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.aurine.cloudx.estate.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 缴费记录
 *
 * @author 黄阳光
 * @date 2020-07-10 09:49:12
 */
@Data
@TableName("project_park_billing_info")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "缴费记录")
public class ProjectParkBillingInfoVo extends Model<ProjectParkBillingInfoVo> {
    private static final long serialVersionUID = 1L;

    /**
     * 支付订单号
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "支付订单号")
    private String payOrderNo;
    /**
     * 第三方支付订单号
     */
    @ApiModelProperty(value = "第三方支付订单号")
    private String payOrderCode;
    /**
     * 第三方停车订单号
     */
    @ApiModelProperty(value = "第三方停车订单号")
    private String parkOrderCode;
    /**
     * 停车场ID
     */
    @ApiModelProperty(value = "停车场ID")
    private String parkId;
    /**
     * 车场名称
     */
    @ApiModelProperty(value = "车场名称")
    private String parkName;
    /**
     * 车牌号
     */
    @ApiModelProperty(value = "车牌号")
    private String plateNumber;
    /**
     * 订单类型 1 月租车充值 2 临时车缴费
     */
    @ApiModelProperty(value = "订单类型 1 月租车充值 2 临时车缴费")
    private String orderType;
    /**
     * 应收金额
     */
    @ApiModelProperty(value = "应收金额")
    private BigDecimal receivable;
    /**
     * 支付金额
     */
    @ApiModelProperty(value = "支付金额")
    private BigDecimal payment;
    /**
     * 支付时间
     */
    @ApiModelProperty(value = "支付时间")
    private LocalDateTime payTime;
    /**
     * 支付方式 1 现金/转账 2 线下微信 3 线下支付宝 4 微信 5 支付宝 6 其他
     */
    @ApiModelProperty(value = "支付方式 1 现金/转账 2 线下微信 3 线下支付宝 4 微信 5 支付宝 6 其他")
    private String payType;
    /**
     * 入场时间
     */
    @ApiModelProperty(value = "入场时间")
    private LocalDateTime enterTime;
    /**
     * 出场时间
     */
    @ApiModelProperty(value = "出场时间")
    private LocalDateTime outTime;
    /**
     * 停车订单号
     */
    @ApiModelProperty(value = "停车订单号")
    private String parkOrderNo;
    /**
     * 查询开始时间
     */
    @ApiModelProperty(value = "查询开始时间")
    private LocalDateTime effTime;
    /**
     * 查询结束时间
     */
    @ApiModelProperty(value = "查询结束时间")
    private LocalDateTime expTime;

    /**
     * 项目id
     */
    @ApiModelProperty("项目id")
    private Integer projectId;

    /**
     *
     */
    @ApiModelProperty(value = "", hidden = true)
    @TableField(value = "tenant_id")
    private Integer tenantId;
}
