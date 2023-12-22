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

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 周界报警布防/撤防记录表
 *
 * @author pigx code generator
 * @date 2022-07-04 16:10:18
 */
@Data
@TableName("project_perimeter_alarm_record")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "周界报警布防/撤防记录表")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectPerimeterAlarmRecord extends Model<ProjectPerimeterAlarmRecord> {
private static final long serialVersionUID = 1L;

    /**
     * 序列，自增
     */
    @ApiModelProperty(value="序列，自增")
    private Integer seq;
    /**
     * 记录id，uuid
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value="记录id，uuid")
    private String recordId;
    /**
     * 报警主机设备id
     */
    @ApiModelProperty(value="报警主机设备id")
    private String deviceId;
    /**
     * 报警主机设备名称
     */
    @ApiModelProperty(value="报警主机设备名称")
    private String deviceName;
    /**
     * 报警模块设备号
     */
    @ApiModelProperty(value="报警模块设备号")
    private String moduleNo;
    /**
     * 区域名称
     */
    @ApiModelProperty(value="区域名称")
    private String regionName;
    /**
     * 防区号
     */
    @ApiModelProperty(value="防区号")
    private String channelNo;
    /**
     * 防区名称
     */
    @ApiModelProperty(value="防区名称")
    private String channelName;
    /**
     * 操作类型 0 撤防 1 布防
     */
    @ApiModelProperty(value="操作类型 0 撤防 1 布防")
    private String operateType;
    /**
     * 操作状态 0 失败 1 成功
     */
    @ApiModelProperty(value="操作状态 0 失败 1 成功")
    private String operateStatus;
    /**
     * 项目id
     */
    @ApiModelProperty(value="项目id")
    private Integer projectId;
    /**
     * 
     */
    @ApiModelProperty(value="",hidden=true)
    private Integer tenantId;
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
