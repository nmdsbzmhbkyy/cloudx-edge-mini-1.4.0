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
 * @author pigx code generator
 * @date 2021-12-22 16:46:12
 */
@Data
@TableName("task_info")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "流水记录表")
public class TaskInfo extends Model<TaskInfo> {
    private static final long serialVersionUID = 1L;

    /**
     * 自增序列
     */
    @ApiModelProperty(value = "自增序列", notes = "自增主键seq")
    private Integer seq;

    /**
     * 随机生成的32位主键
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "唯一主键", notes = "随机生成的32位唯一主键")
    private String taskId;

    /**
     * 租户id
     */
    @ApiModelProperty(value = "租户id", notes = "租户id")
    private Integer tenantId;

    /**
     * 项目Uuid
     */
    @ApiModelProperty(value = "项目Uuid", notes = "项目Uuid")
    private String projectUUID;

    /**
     * 业务主键
     */
    @ApiModelProperty(value = "业务主键", notes = "云平台或者边缘网关变动的表的主键")
    private String uuid;

    /**
     * 操作，级联，事件，命令及其他类型的类型值
     */
    @ApiModelProperty(value = "类型", notes = "操作，级联，事件，命令及其他类型的类型值")
    private String type;
    /**
     * 服务类型
     * 级联入云：cascade
     * 操作：operate
     * 事件：event
     * 命令：command
     * 其他：ohter
     */
    @ApiModelProperty(value = "服务类型", notes = "级联入云：cascade，操作：operate，事件：event，命令：command，其他：ohter")
    private String serviceType;
    /**
     * 服务名称
     */
    @ApiModelProperty(value = "服务名称", notes = "服务名称")
    private String serviceName;

    /**
     * 状态
     * 0：待发送
     * 1：已发送未确认
     * 2：已确认
     * 默认值：0
     */
    @ApiModelProperty(value = "状态", notes = "发送状态 0：待发送，1：已发送未确认，2：已确认，默认值：0")
    private String state = "0";

    /**
     * 错误信息
     */
    @ApiModelProperty(value = "错误信息")
    private String errMsg;

    /**
     * 重试次数
     */
    @ApiModelProperty(value = "重试次数")
    private Integer retriesCount;

    /**
     * 最后一次报错时间
     */
    @ApiModelProperty(value = "最后一次报错时间")
    private LocalDateTime lastErrTime;

    /**
     * 新的MD5
     */
    @ApiModelProperty(value = "新的MD5")
    private String newMd5;
    /**
     * 操作的Json对象字符串
     */
    @ApiModelProperty(value = "操作的Json对象字符串")
    private String data;

    /**
     * 数据来源
     * 云端：platform
     * 主边缘网关：master
     * 子边缘网关：slave
     */
    @ApiModelProperty(value = "数据来源")
    private String source;

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
