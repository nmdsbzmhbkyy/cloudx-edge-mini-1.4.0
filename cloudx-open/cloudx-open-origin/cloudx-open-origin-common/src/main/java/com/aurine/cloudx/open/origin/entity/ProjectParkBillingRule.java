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

package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 车场计费规则
 *
 * @author 王伟
 * @date 2020-07-07 11:34:12
 */
@Data
@TableName(value = "project_park_billing_rule", schema = "aurine_parking")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "车场计费规则")
public class ProjectParkBillingRule extends OpenBasePo<ProjectParkBillingRule> {
    private static final long serialVersionUID = 1L;


    /**
     * 规则ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "规则ID")
    private String ruleId;
    /**
     * 第三方规则id
     */
    @ApiModelProperty(value = "第三方规则id")
    private String ruleCode;
    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    private Integer projectId;
    /**
     * 规则名称
     */
    @ApiModelProperty(value = "规则名称")
    private String ruleName;

    /**
     * 车辆类型ID
     */
    @ApiModelProperty(value = "车辆类型ID")
    private String carTypeId;
    /**
     * 规则类型 1 免费车 2 月租车 3 临时车
     */
    @ApiModelProperty(value = "规则类型 1 免费车 2 月租车 3 临时车")
    private String ruleType;
    /**
     * 车场ID
     */
    @ApiModelProperty(value = "车场ID")
    private String parkId;
    /**
     * 是否禁用 0 启用 1 禁用
     */
    @ApiModelProperty(value = "是否禁用 0 启用 1 禁用")
    private String isDisable;

    /**
     * 是否默认
     */
    @ApiModelProperty(value = "是否默认 0 否 1 是")
    private Integer isDefault;
    /**
     * （仅适用规则为临时）计费模板 1 标准24小时收费 2 标准分段收费
     */
    @ApiModelProperty(value = "（仅适用规则为临时）计费模板 1 标准24小时收费 2 标准分段收费")
    private String ruleTemplate;
    /**
     * 月租费用（元）
     */
    @ApiModelProperty(value = "月租费用（元）")
    private BigDecimal monthlyRent;

    /**
     *
     */
    @TableField(value = "tenant_id")
    private Integer tenantId;
    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    @TableField(fill = FieldFill.INSERT)
    private Integer operator;
    /**
     * 操作时间
     */
    @ApiModelProperty(value = "操作时间")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
}
