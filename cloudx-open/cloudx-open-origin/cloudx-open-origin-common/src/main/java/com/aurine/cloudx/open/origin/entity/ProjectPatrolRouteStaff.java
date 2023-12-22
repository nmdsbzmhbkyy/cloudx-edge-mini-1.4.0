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
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 巡更路线参与人列表
 *
 * @author wangwei
 * @date 2020-10-27 15:26:07
 */
@Data
@TableName("project_patrol_route_staff")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "巡更路线参与人列表")
public class ProjectPatrolRouteStaff extends OpenBasePo<ProjectPatrolRouteStaff> {
    private static final long serialVersionUID = 1L;

    /**
     * 自增序列
     */
    @TableId
    @ApiModelProperty(value = "自增序列")
    private Integer seq;

    /**
     * 巡更路线id，uuid
     */
    @ApiModelProperty(value = "巡更路线id，uuid")
    private String patrolRouteId;
    /**
     * 员工id
     */
    @ApiModelProperty(value = "员工id")
    private String staffId;
    /**
     * 偏移量
     */
    @ApiModelProperty(value = "偏移量")
    private String staffOffset;
//    /**
//     * 项目id
//     */
//    @ApiModelProperty(value="项目id")
//    private Integer projectId;
}
