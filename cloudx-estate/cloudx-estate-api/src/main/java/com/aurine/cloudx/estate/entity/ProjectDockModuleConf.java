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

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 第三方系统配置
 *
 * @author 王伟
 * @date 2020-12-15 13:39:38
 */
@Data
@TableName("project_dock_module_conf")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "第三方系统配置")
public class ProjectDockModuleConf extends Model<ProjectDockModuleConf> {
private static final long serialVersionUID = 1L;

    /**
     * 序列，自增
     */
    @TableId
    @ApiModelProperty(value="序列，自增")
    private Integer seq;
    /**
     * 项目id
     */
    @ApiModelProperty(value="项目id")
    private Integer projectId;
    /**
     * 模块id，如WR20，内部使用，不要用uuid
     */
    @ApiModelProperty(value="模块id，如WR20，内部使用，不要用uuid")
    private String moduleId;
    /**
     * 模块名称，如WR20，可用于展示
     */
    @ApiModelProperty(value="模块名称，如WR20，可用于展示")
    private String moduleName;
    /**
     * 是否启用
     */
    @ApiModelProperty(value="是否启用")
    private String isActive;
    /**
     * 模块配置json字符串
     */
    @ApiModelProperty(value="模块配置json字符串")
    private String jsonConfig;
    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value="更新时间")
    private LocalDateTime updateTime;
    /**
     * 最后操作人
     */
    @ApiModelProperty(value="最后操作人")
    private Integer operator;

    }
