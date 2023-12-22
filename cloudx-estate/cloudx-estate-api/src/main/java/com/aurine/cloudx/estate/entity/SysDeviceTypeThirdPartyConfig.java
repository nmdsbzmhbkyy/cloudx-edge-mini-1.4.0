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

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * 第三方接口设备配置
 *
 * @author 王伟
 * @date 2020-08-13 14:23:08
 */
@Data
@TableName("sys_devicetype_thirdparty_config")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "第三方接口设备配置")
public class SysDeviceTypeThirdPartyConfig extends Model<SysDeviceTypeThirdPartyConfig> {
    private static final long serialVersionUID = 1L;

    /**
     * 序列，自增
     */
    @TableId
    @ApiModelProperty(value = "序列，自增")
    private Integer seq;

    /**
     * 设备类型id，字典device_type
     */
    @ApiModelProperty(value = "设备类型id，字典device_type")
    private String deviceTypeId;
    /**
     * 第三方id
     */
    @ApiModelProperty(value = "第三方id,存储接口配置id")
    private String thirdPartyNo;
    /**
     * 是否默认 1 是 0 否
     */
    @ApiModelProperty(value = "是否默认 1 是 0 否")
    private String isDefault;
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

    public static void main(String[] args) {
        ArrayList<String> nums = new ArrayList<>();
        Iterator<String> iterator = nums.iterator();
        ListIterator<String> stringListIterator = nums.listIterator();
    }
}
