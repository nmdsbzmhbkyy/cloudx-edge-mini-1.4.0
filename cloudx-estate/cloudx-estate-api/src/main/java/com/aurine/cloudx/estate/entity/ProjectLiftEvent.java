

package com.aurine.cloudx.estate.entity;

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
 * 乘梯事件记录
 *
 * @author 黄健杰 code generator
 * @date 2022-07-08 15:57:22
 */
@Data
@TableName("project_lift_event")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "乘梯事件记录")
public class ProjectLiftEvent extends Model<ProjectLiftEvent> {
private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value="乘梯事件uuid")
    private String eventId;

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
//    @TableField(fill = FieldFill.INSERT)
    private Integer operator;
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
     *
     */
    @ApiModelProperty(value = "", hidden = true)
    @TableField(value = "tenant_id")
    private Integer tenantId;
    }
