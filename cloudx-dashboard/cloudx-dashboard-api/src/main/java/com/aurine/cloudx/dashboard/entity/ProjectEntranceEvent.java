

package com.aurine.cloudx.dashboard.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 通行事件记录
 *
 * @author 黄阳光 code generator
 * @date 2020-05-20 13:27:59
 */
@Data
@TableName("PROJECT_ENTRANCE_EVENT")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "通行事件记录")

public class ProjectEntranceEvent extends Model<ProjectEntranceEvent> {
private static final long serialVersionUID = 1L;

    /**
     * 序列
     */
    @TableId
    @ApiModelProperty(value="序列")
    private Long seq;
    /**
     * 项目id
     */
    @ApiModelProperty(value="项目id")
    private Integer projectId;
    /**
     * 人员类型 1 住户 2 员工 3 访客
     */
    @ApiModelProperty(value="人员类型 1 住户 2 员工 3 访客")
    private String personType;
    /**
     * 人员id，根据人员类型取相应表id，注意访客应取当前的来访id
     */
    @ApiModelProperty(value="人员id，根据人员类型取相应表id，注意访客应取当前的来访id")
    private String personId;
    /**
     * 人员姓名
     */
    @ApiModelProperty(value="人员姓名")
    private String personName;
    /**
     * 事件时间
     */
    @ApiModelProperty(value="事件时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventTime;
    /**
     * 设备id
     */
    @ApiModelProperty(value="设备id")
    private String deviceId;
    /**
     * 设备名称
     */
    @ApiModelProperty(value = "设备名称")
    private String deviceName;
    /**
     * 设备区域名称
     */
    @ApiModelProperty(value = "设备区域名称")
    private String deviceRegionName;
    /**
     * 出入方向 1 入  2 出
     */
    @ApiModelProperty(value="出入方向 1 入  2 出")
    private String entranceType;
    /**
     * 事件类型
     */
    @ApiModelProperty(value="事件类型")
    private String eventType;
    /**
     * 认证介质 1 指纹 2 人脸 3 卡
     */
    @ApiModelProperty(value="认证介质 1 指纹 2 人脸 3 卡")
    private String certMedia;
    /**
     * 图片路径
     */
    @ApiModelProperty(value="图片路径")
    private String picUrl;
    /**
     * 事件描述
     */
    @ApiModelProperty(value="事件描述")
    private String eventDesc;

    /**
     * 操作人
     */
    @ApiModelProperty(value="操作人")
    private Integer operator;
    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value="更新时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     *
     */
    @ApiModelProperty(value = "", hidden = true)
    @TableField(value = "TENANT_ID")
    private Integer tenantId;
    }
