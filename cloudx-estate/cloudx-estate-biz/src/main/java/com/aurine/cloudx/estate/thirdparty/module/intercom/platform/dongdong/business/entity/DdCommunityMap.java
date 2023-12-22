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

package com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.business.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 社区映射
 *
 * @author 王伟
 * @date 2020-11-18 17:13:14
 */
@Data
@TableName("dd_community_map")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "社区映射")
public class DdCommunityMap extends Model<DdCommunityMap> {
private static final long serialVersionUID = 1L;

    /**
     * 序列，自增
     */
    @TableId
    @ApiModelProperty(value="序列，自增")
    private Integer seq;
    /**
     * 咚咚社区id
     */
    @ApiModelProperty(value="咚咚社区id")
    private Integer communityid;
    /**
     * 社区名称
     */
    @ApiModelProperty(value="社区名称")
    private String communityname;
    /**
     * 省市县字符串
     */
    @ApiModelProperty(value="省市县字符串")
    private String addr;
    /**
     * 详细地址
     */
    @ApiModelProperty(value="详细地址")
    private String detailaddr;
    /**
     * 4.0的项目id
     */
    @ApiModelProperty(value="4.0的项目id")
    private Integer projectid;
    /**
     * 4.0的租户id
     */
    @TableField(value = "tenant_id")
    @ApiModelProperty(value="4.0的租户id",hidden=true)
    private Integer tenantId;
    /**
     * 创建人
     */
    @ApiModelProperty(value="创建人")
    private Integer operator;
    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createtime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value="更新时间")
    private LocalDateTime updatetime;
    }
