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

import com.baomidou.mybatisplus.annotation.IdType;
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
 * 第三方接口配置
 *
 * @author 王伟
 * @date 2020-08-13 14:20:04
 */
@Data
@TableName("sys_thirdparty_interface_config")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "第三方接口配置")
public class SysThirdPartyInterfaceConfig extends Model<SysThirdPartyInterfaceConfig> {
    private static final long serialVersionUID = 1L;

    /**
     * 序列，自增
     */
    @ApiModelProperty(value = "序列，自增")
    private Integer seq;

    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "主键")
    private String thirdPartyNo;
    /**
     * 名称
     */

    @ApiModelProperty(value = "名称")
    private String name;
    /**
     * 版本
     */
    @ApiModelProperty(value = "版本")
    private String version;
    /**
     * 配置串
     */
    @ApiModelProperty(value = "配置串")
    private String jsonConfig;
    /**
     * 接口类型 1 平台级 2 项目级
     */
    @ApiModelProperty(value = "接口类型 1 平台级 2 项目级")
    private String type;
    /**
     * 订阅状态  0 否 1是
     */
    @ApiModelProperty(value = "订阅状态 0 否 1是")
    private String subscribeStatus;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
    /**
     * 项目id，为空则为全局配置
     */
    @ApiModelProperty(value = "项目id，为空则为全局配置")
    private Integer projectId;
    /**
     *
     */
    @ApiModelProperty(value = "", hidden = true)
    @TableField("tenant_id")
    private Integer tenantId;
    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
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
