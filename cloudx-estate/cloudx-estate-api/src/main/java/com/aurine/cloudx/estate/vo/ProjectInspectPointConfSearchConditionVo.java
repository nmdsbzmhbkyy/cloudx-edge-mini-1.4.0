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

package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 巡检点搜索条件
 * </p>
 *
 * @author 王良俊
 */
@Data
@ApiModel(value = "设备巡检点查询条件vo对象")
public class ProjectInspectPointConfSearchConditionVo {

    /**
     * 巡检点名称
     */
    @ApiModelProperty(value = "巡检点名称")
    private String pointName;

    /**
     * 巡检点制定时间范围数组
     */
    @ApiModelProperty(value = "巡检点制定时间范围数组")
    private String[] dateRange;

    /**
     * 已经添加了的巡检点ID用于进行排除
     */
    @ApiModelProperty(value = "已经添加了的巡检点ID用于进行排除")
    private String[] pointIdList;

//    /**
//     * 巡检点地址
//     * */
//    @ApiModelProperty(value = "巡点地址")
//    private String address;

}
