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

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 车辆登记查询条件
 *
 * @author 王伟
 * @date 2020-07-08 14:10:32
 */
@Data
@ApiModel(value = "车辆登记VO")
public class ProjectParCarRegisterSeachConditionVo {

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
     * 车主名
     */
    @ApiModelProperty(value = "车主名")
    private String personName;

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
     * 车位类型
     */
    @ApiModelProperty(value = "车位类型")
    private String relType;
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
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
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
