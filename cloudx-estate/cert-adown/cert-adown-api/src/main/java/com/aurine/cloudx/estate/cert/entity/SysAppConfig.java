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

package com.aurine.cloudx.estate.cert.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * APP配置
 *
 * @author wangwei
 * @date 2021-12-16 08:31:33
 */
@Data
@TableName("sys_app_config")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "APP配置")
public class SysAppConfig extends Model<SysAppConfig> {
    private static final long serialVersionUID = 1L;

    /**
     * 主键，自增
     */
    @TableId
    @ApiModelProperty(value = "主键，自增")
    private Integer seq;
    /**
     * 1=云平台
     */
    @ApiModelProperty(value = "1=云平台")
    private String appId;
    /**
     *
     */
    @ApiModelProperty(value = "")
    private String appName;
    /**
     *
     */
    @ApiModelProperty(value = "")
    private String reqCallBackUrl;
    /**
     *
     */
    @ApiModelProperty(value = "")
    private String respCallBackUrl;

}
