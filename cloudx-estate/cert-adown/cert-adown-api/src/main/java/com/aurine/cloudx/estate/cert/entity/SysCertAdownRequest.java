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

package com.aurine.cloudx.estate.cert.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 下载任务请求
 *
 * @author wangwei
 * @date 2021-12-15 13:45:44
 */
@Data
@TableName("sys_cert_adown_request")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "")
public class SysCertAdownRequest extends Model<SysCertAdownRequest> {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键，自增
	 */

	@ApiModelProperty(value = "主键，自增")
	private Integer seq;
	/**
	 * 使用msgId
	 */
	@TableId(type = IdType.ASSIGN_UUID)
	@ApiModelProperty(value = "使用msgId")
	private String requestId;
	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private String projectId;
	/**
	 * 请求来源应用 1:Cloudx 4.0
	 */
	@ApiModelProperty(value = "1:Cloudx 4.0")
	private String appId;
	/**
	 * 平台ID 调用下发的平台，主要用于流量控制 2:中台2.0，3:中台3.0 - 华为平台
	 */
	@ApiModelProperty(value = "平台ID 调用下发的平台，主要用于流量控制 2:中台2.0，3:中台3.0 - 华为平台")
	private String platformId;
	/**
	 * 0最优先，预留，1：优先下载，2：次优先，3：低优先任务
	 */
	@ApiModelProperty(value = "0最优先，预留，1：优先下载，2：次优先，3：低优先任务")
	private Integer priotity;
	/**
	 *
	 */
	@ApiModelProperty(value = "序列化请求内容")
	private String body;
	/**
	 * 0:待下载;2:下载中;3:已下载;4:下载失败;5:超时
	 */
	@ApiModelProperty(value = "0:待下载;2:下载中;3:已下载;4:下载失败;5:超时")
	private String state;

	@ApiModelProperty(value = "异常信息")
	private String errMsg;

	/**
	 * 设备ID 设备唯一标识
	 */
	@ApiModelProperty(value = "设备ID 设备唯一标识")
	private String deviceId;

	/**
	 * 设备名称
	 */
	@ApiModelProperty(value = "设备名称")
	private String deviceName;

	/**
	 * 0:卡片，1:人脸
	 */
	@ApiModelProperty(value = "0:卡片，1:人脸")
	private String certMediaType;

	/**
	 * 初始请求时间
	 */
	@ApiModelProperty(value = "初始请求时间")
	private LocalDateTime requestTime;
	/**
	 * 进入下载中的时间
	 */
	@ApiModelProperty(value = "进入下载中的时间")
	private LocalDateTime downloadingTime;
	/**
	 * 最终返回的时间
	 */
	@ApiModelProperty(value = "最终返回的时间")
	private LocalDateTime responseTime;

	/**
	 * 超时重试次数
	 * */
	@ApiModelProperty(value = "超时重试次数")
	@TableField(exist = false)
	private int retry;

//    /**
//     * 创建时间
//     */
//    @ApiModelProperty(value="创建时间")
//    private LocalDateTime createTime;
//    /**
//     * 修改时间
//     */
//    @ApiModelProperty(value="修改时间")
//    private LocalDateTime updateTime;
}
