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
 * 车位区域表
 *
 * @author 王良俊
 * @date 2020-07-07 11:00:29
 */
@Data
@TableName(value = "project_park_region", schema = "aurine_parking")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "车位区域表")
public class ProjectParkRegion extends OpenBasePo<ProjectParkRegion> {
    private static final long serialVersionUID = 1L;


    /**
     * 车位区域id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "车位区域id")
    private String parkRegionId;
    /**
     * 停车场ID
     */
    @ApiModelProperty(value = "停车场ID")
    private String parkId;
    /**
     * 车位区域名
     */
    @ApiModelProperty(value = "车位区域名")
    private String parkRegionName;
    /**
     * 停车位总数
     */
    @ApiModelProperty(value = "停车位总数")
    private Integer parkNum;
    /**
     * 是否公共区域 1 是 0 否
     */
    @ApiModelProperty(value = "是否公共区域 1 是 0 否")
    private String isPublic;
    /**
     * 楼层
     */
    @ApiModelProperty(value = "楼层")
    private String floor;
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
