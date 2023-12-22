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

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 设备产品
 *
 * @author 王伟
 * @date 2020-10-26 11:46:15
 */
@Data
@TableName("sys_device_product_map")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "设备产品")
public class SysDeviceProductMap extends Model<SysDeviceProductMap> {
private static final long serialVersionUID = 1L;

    /**
     * 序列，自增
     */
//    @TableId
    @ApiModelProperty(value="序列，自增")
    private Integer seq;
    /**
     * 设备类型id，来源sys_dict配置的设备类型
     */
    @ApiModelProperty(value="设备类型id，来源sys_dict配置的设备类型")
    private String deviceTypeId;

    /**
     * 产品id 4.0平台自用，与设备表关联（这是自增序列不使用这个，要使用productKey）
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value="产品id 4.0平台自用，与设备表关联")
    private String productId;
    /**
     * 第三方产品唯一编码
     */
    @ApiModelProperty(value="第三方产品唯一编码")
    private String productCode;
    /**
     * 产品品类
     */
    @ApiModelProperty(value="产品品类")
    private String productType;
    /**
     * 第三方类型名称
     */
    @ApiModelProperty(value="第三方类型名称")
    private String productName;
    /**
     * 产品型号
     */
    @ApiModelProperty(value="产品型号")
    private String productModel;
    /**
     * 产品型号id
     */
    @ApiModelProperty(value="产品型号id")
    private String modelId;
    /**
     * 厂商
     */
    @ApiModelProperty(value="厂商")
    private String manufacture;
    /**
     * 产品描述
     */
    @ApiModelProperty(value="产品描述")
    private String productDesc;
    /**
     * 协议类型
     */
    @ApiModelProperty(value="协议类型")
    private String protocal;
    /**
     * 数据格式
     */
    @ApiModelProperty(value="数据格式")
    private String dataFormat;
    /**
     * 行业
     */
    @ApiModelProperty(value="行业")
    private String industry;
    /**
     * 能力集
     */
    @ApiModelProperty(value="能力集")
    private String capabilities;
    /**
     * 能力
     */
    @ApiModelProperty(value="能力")
    private String capability;
    /**
     * 接口适配id
     */
    @ApiModelProperty(value="接口适配id")
    private String adaptionId;
    /**
     * 第三方id
     */
    @ApiModelProperty(value="第三方id")
    private String thirdPartyNo;
    /**
     * 项目id
     */
    @ApiModelProperty(value="项目id")
    private Integer projectId;
//    /**
//     *
//     */
//    @ApiModelProperty(value="",hidden=true)
//    private Integer tenantId;
    /**
     * 操作人
     */
    @ApiModelProperty(value="操作人")
    private Integer operator;
    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value="更新时间")
    private LocalDateTime updateTime;
    }
