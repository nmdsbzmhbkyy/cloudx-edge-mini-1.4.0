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

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 *
 * @author pigx code generator
 * @date 2021-07-28 15:29:36
 */
@Data
@TableName("sys_decided")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "记录订阅表")
public class SysDecided extends Model<SysDecided> {
private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId
    @ApiModelProperty(value="id")
    private Integer id;
    /**
     * 订阅人，可以为空
     */
    @ApiModelProperty(value="订阅人，可以为空")
    private Integer userid;
    /**
     * 订阅项目
     */
    @ApiModelProperty(value="订阅项目")
    private Integer projectid;
    /**
     * 订阅数据响应方式，冗余字段
     */
    @ApiModelProperty(value="订阅数据响应方式，冗余字段")
    private String resptype;
    /**
     * 订阅数据类型（1表示车行数据，2表示人行数据）
     */
    @ApiModelProperty(value="订阅数据类型（1表示车行数据，2表示人行数据，3表示告警数据）")
    private String type;
    /**
     * 通知地址（先用简单的方式实现）
     */
    @ApiModelProperty(value="通知地址（先用简单的方式实现）")
    private String addr;
    }
