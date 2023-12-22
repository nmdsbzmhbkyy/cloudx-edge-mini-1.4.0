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

import java.time.LocalDateTime;

/**
 * 车辆登记
 *
 * @author 王伟
 * @date 2020-07-08 14:10:32
 */
@Data
@TableName(value = "project_par_car_register", schema = "aurine_parking")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "车辆登记")
public class ProjectParCarRegister extends OpenBasePo<ProjectParCarRegister> {
    private static final long serialVersionUID = 1L;


    /**
     * 登记id，uuid
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "登记id，uuid")
    private String registerId;
    /**
     * 车辆信息uid
     */
    @ApiModelProperty(value = "车辆信息Uid")
    private String carUid;
    /**
     * 车牌号
     */
    @ApiModelProperty(value = "车牌号")
    private String plateNumber;
    /**
     * 停车场ID
     */
    @ApiModelProperty(value = "停车场ID")
    private String parkId;
    /**
     * 车位id
     */
    @ApiModelProperty(value = "车位id")
    private String parkPlaceId;
    /**
     * 车位地址
     */
    @ApiModelProperty(value = "车位地址")
    private String parkPlaceName;
    /**
     * 开始日期
     */
    @ApiModelProperty(value = "开始日期")
    private LocalDateTime startTime;
    /**
     * 结束日期
     */
    @ApiModelProperty(value = "结束日期")
    private LocalDateTime endTime;
    /**
     * 是否注销 0 正常 1 已注销
     */
    @ApiModelProperty(value = "是否注销 0 正常 1 已注销")
    private String isCancelled;

    /**
     * 可通行车道ID数组 String
     */
    @ApiModelProperty(value = "可通行车道ID数组 String")
    private String laneList;

    /**
     * 月租车收费规则id，关联project_park_billing_rule 因为一位多车调整导致收费规则需要与车辆直接绑定，而不是原本的车位
     */
    @ApiModelProperty(value = "月租车收费规则id，关联project_park_billing_rule")
    private String ruleId;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    @TableField(fill = FieldFill.INSERT)
    private Integer operator;
    /**
     * 租户id
     */
    @TableField("tenant_id")
    @ApiModelProperty(value = "租户id")
    private Integer tenantId;
    /**
     * 项目ID
     */
    @ApiModelProperty(value = "项目ID")
    private Integer projectId;
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
