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
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 人员属性拓展表
 *
 * @author xull@aurine.cn
 * @date 2020-07-03 16:24:30
 */
@Data
@TableName("project_person_attr")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "人员属性拓展表")
public class ProjectPersonAttr extends OpenBasePo<ProjectPersonAttr> {
    private static final long serialVersionUID = 1L;

    /**
     * 属性id
     */
    @ApiModelProperty(value = "属性id")
    private String attrId;
    /**
     * 人员id
     */
    @ApiModelProperty(value = "人员id")
    private String personId;
    /**
     * 人员类型 1 住户 2 员工 3 访客
     */
    @ApiModelProperty(value = "人员类型 1 住户 2 员工 3 访客")
    private String personType;

    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    private Integer projectId;


    /**
     * 属性值
     */
    @ApiModelProperty(value = "属性值")
    private String attrValue;
}
