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

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 人员属性拓展配置表
 *
 * @author xull@aurine.cn
 * @date 2020-07-03 16:24:19
 */
@Data
@TableName("project_person_attr_conf")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "人员属性拓展配置表")
public class ProjectPersonAttrConf extends Model<ProjectPersonAttrConf> {
    private static final long serialVersionUID = 1L;

    /**
     * 属性id
     */
    @ApiModelProperty(value = "属性id")
    @TableId(type = IdType.ASSIGN_UUID)
    private String attrId;
    /**
     * 人员类型 1 住户 2 员工 3 访客
     */
    @ApiModelProperty(value = "人员类型 1 住户 2 员工 3 访客")
    private String personType;

    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer projectId;

    /**
     * 属性编码
     */
    @ApiModelProperty(value = "属性编码")
    private String attrCode;
    /**
     * 属性名称
     */
    @ApiModelProperty(value = "属性名称")
    private String attrName;
    /**
     * 值描述
     */
    @ApiModelProperty(value = "值描述")
    private String remark;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private Integer operator;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
}
