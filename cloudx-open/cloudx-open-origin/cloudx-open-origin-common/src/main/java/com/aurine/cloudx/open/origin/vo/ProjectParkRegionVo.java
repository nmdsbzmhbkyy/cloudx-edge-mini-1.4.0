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

package com.aurine.cloudx.open.origin.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 车位区域表
 *
 * @author 王良俊
 * @date 2020-07-07 11:00:29
 */
@Data
@TableName("project_park_region")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "车位区域表")
public class ProjectParkRegionVo extends Model<ProjectParkRegionVo> {
private static final long serialVersionUID = 1L;

    /**
     * 车位区域名
     */
    @ApiModelProperty(value="车位区域名")
    private String parkRegionName;
    /**
     * 车位区域Id
     */
    @ApiModelProperty(value="车位区域Id")
    private String parkRegionId;
    /**
     * 车位区域Id
     */
    @ApiModelProperty(value="停车场Id")
    private String parkId;
    /**
     * 停车位总数
     */
    @ApiModelProperty(value="停车位总数")
    private Integer parkNum;
    /**
     * 已使用车位数
     */
    @ApiModelProperty(value="已使用车位数")
    private Integer usedPark;
    /**
     * 是否公共区域 1 是 0 否
     */
    @ApiModelProperty(value="是否公共区域 1 是 0 否")
    private String isPublic;
    }
