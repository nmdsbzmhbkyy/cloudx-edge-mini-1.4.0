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

/**
 * 设备巡检点配置
 *
 * @author 王良俊
 * @date 2020-07-23 16:26:33
 */
@Data
@TableName("project_inspect_point_conf")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "设备巡检点配置")
public class ProjectInspectPointConf extends OpenBasePo<ProjectInspectPointConf> {
    private static final long serialVersionUID = 1L;

    /**
     * 自增序列
     */
    @ApiModelProperty(value = "自增序列")
    private Integer seq;

    /**
     * 巡检点id，uuid
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "巡检点id，uuid")
    private String pointId;

    /**
     * 巡检点名称
     */
    @ApiModelProperty(value = "巡检点名称")
    private String pointName;

    /**
     * 所在位置
     */
    @ApiModelProperty(value = "所在位置")
    private String regionId;

    /**
     * 是否使用蓝牙签到 1 是 0 否
     */
    @ApiModelProperty(value = "是否使用蓝牙签到 1 是 0 否")
    private String isBlueTeeth;

    /**
     * 蓝牙设备编号
     */
    @ApiModelProperty(value = "蓝牙设备编号")
    private String deviceId;

    /**
     * 温度(单位：℃) 1 是 0 否
     */
    @ApiModelProperty(value = "温度(单位：℃) 1 是 0 否")
    private String temperature;

    /**
     * 湿度(单位： %RH) 1 是 0 否
     */
    @ApiModelProperty(value = "湿度(单位： %RH) 1 是 0 否")
    private String humidity;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 状态 0 已停用 1 已启用
     */
    @ApiModelProperty(value = "状态 0 已停用 1 已启用")
    private char status;
}
