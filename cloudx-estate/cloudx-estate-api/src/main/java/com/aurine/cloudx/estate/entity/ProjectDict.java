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

import com.baomidou.mybatisplus.annotation.FieldFill;
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
 * 项目字典表
 *
 * @author cjw
 * @date 2021-07-07 08:38:37
 */
@Data
@TableName("project_dict")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "项目字典表")
public class ProjectDict extends Model<ProjectDict> {
private static final long serialVersionUID = 1L;

    /**
     * 序列
     */
    @TableId
    @ApiModelProperty(value="序列")
    private Integer seq;
    /**
     * 字典组编码
     */
    @ApiModelProperty(value="字典组编码")
    private String dictGroupCode;
    /**
     * 字典值排序
     */
    @ApiModelProperty(value="字典值排序")
    private Integer dictSeq;
    /**
     * 字典编码
     */
    @ApiModelProperty(value="字典编码")
    private String dictCode;
    /**
     * 字典值
     */
    @ApiModelProperty(value="字典值")
    private String dictValue;
    /**
     * 项目ID
     */
    @ApiModelProperty(value="项目ID")
    private Integer projectId;
    /**
     * 状态 0 禁用 1 启用
     */
    @ApiModelProperty(value="状态 0 禁用 1 启用")
    private String status;
    /**
     * 备注
     */
    @ApiModelProperty(value="备注")
    private String remark;
    /**
     * 操作人
     */
    @ApiModelProperty(value="操作人")
    @TableField(fill = FieldFill.INSERT)
    private Integer operator;
    /**
     * 操作时间
     */
    @ApiModelProperty(value="操作时间")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value="更新时间")
    private LocalDateTime updateTime;
    }
