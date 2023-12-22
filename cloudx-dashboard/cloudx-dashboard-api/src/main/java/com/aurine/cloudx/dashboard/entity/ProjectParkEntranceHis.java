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

package com.aurine.cloudx.dashboard.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 车场管理
 *
 * @author 黄阳光
 * @date 2020-07-07 15:10:26
 */
@Data
@TableName("project_park_entrance_his")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "车场管理")
public class ProjectParkEntranceHis extends Model<ProjectParkEntranceHis> {
    private static final long serialVersionUID = 1L;

    /**
     * 停车订单号
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "停车订单号")
    private String parkOrderNo;
    /**
     * 第三方订单号
     */
    @ApiModelProperty(value = "第三方订单号")
    private String parkOrderCode;
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
     * 入场时间
     */
    @ApiModelProperty(value = "入场时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime enterTime;
    /**
     * 入口名称
     */
    @ApiModelProperty(value = "入口名称")
    private String enterGateName;
    /**
     * 入口操作员
     */
    @ApiModelProperty(value = "入口操作员")
    private String enterOperatorName;
    /**
     * 项目id
     */
    @ApiModelProperty("项目id")
    private Integer projectId;

    /**
     *
     */
    @ApiModelProperty(value = "", hidden = true)
    @TableField(value = "TENANT_ID")
    private Integer tenantId;

    /**
     * 入口车辆图片
     */
    @ApiModelProperty(value = "入口车辆图片")
    private String enterPicUrl;
    /**
     * 出厂时间
     */
    @ApiModelProperty(value = "出场时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime outTime;
    /**
     * 出口名称
     */
    @ApiModelProperty(value = "出口名称")
    private String outGateName;
    /**
     * 出口操作员
     */
    @ApiModelProperty(value = "出口操作员")
    private String outOperatorName;
    /**
     * 出口车辆图片
     */
    @ApiModelProperty(value = "出口车辆图片")
    private String outPicUrl;

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
