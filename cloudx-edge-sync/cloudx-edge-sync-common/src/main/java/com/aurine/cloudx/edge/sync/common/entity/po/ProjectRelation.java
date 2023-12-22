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

package com.aurine.cloudx.edge.sync.common.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author cjw
 * @date 2021-12-23 15:31:43
 */
@Data
@TableName("project_relation")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "")
public class ProjectRelation extends Model<ProjectRelation> {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增主键", hidden = true)
    private Integer seq;

    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "")
    private String relationId;

    @ApiModelProperty(value = "设备SN")
    private String sn;

    @ApiModelProperty(value = "项目UUID")
    private String projectUUID;

    @ApiModelProperty(value = "项目第三方ID")
    private String projectCode;

    @ApiModelProperty(value = "主从类型  0:主  1:从")
    private Integer syncType;

    @ApiModelProperty(value = "本地项目 0：本地项目，1：非本地项目")
    private Integer source;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
}
