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

package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 催缴记录表
 *
 * @author lxl
 * @date 2022-04-28 09:57:00
 */
@Data
@TableName("project_urged_payment")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "催缴记录表")
public class ProjectUrgedPayment extends Model<ProjectUrgedPayment> {
private static final long serialVersionUID = 1L;

    /**
     * 序列
     */
    @TableId
    @ApiModelProperty(value="序列")
    private Integer seq;
    /**
     * 催缴ID
     */
    @ApiModelProperty(value="催缴ID")
    private String urgedId;
    /**
     * 房屋ID
     */
    @ApiModelProperty(value="房屋ID")
    private String houseId;
    /**
     * 项目ID
     */
    @ApiModelProperty(value="项目ID")
    private Integer projectId;
    /**
     * 员工ID
     */
    @ApiModelProperty(value="员工ID")
    private String staffId;
    /**
     * 员工姓名
     */
    @ApiModelProperty(value="员工姓名")
    private String staffName;
    /**
     * 催缴开始时间
     */
    @ApiModelProperty(value="催缴开始时间")
    private LocalDateTime startTime;
    /**
     * 催缴结束时间
     */
    @ApiModelProperty(value="催缴结束时间")
    private LocalDateTime endTime;
    /**
     * 状态 0 进行中 1 已终止 2 已成功
     */
    @ApiModelProperty(value="状态 进行中0  已终止1  已成功2 待分配9")
    private String status;

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
