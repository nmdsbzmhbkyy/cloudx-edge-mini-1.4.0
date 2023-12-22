package com.aurine.cloudx.estate.entity;

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
 * 项目呼叫事件(ProjectDeviceCallEvent)
 *
 * @author : Qiu
 * @date : 2020 12 15 16:15
 */
@Data
@TableName("project_device_call_event")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "项目呼叫事件(ProjectDeviceCallEvent)")
public class ProjectDeviceCallEvent extends Model<ProjectDeviceCallEvent> {
    private static final long serialVersionUID = 1L;

    /**
     * 自增序列
     */
    @ApiModelProperty(value = "自增序列")
    private Integer seq;

    /**
     * 事件id, uuid
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "事件id, uuid")
    private String eventId;

    /**
     * 呼叫类型
     * 0:呼叫业主
     * 1:呼叫中心
     * 2:呼叫物业
     * 3:呼叫转移（云电话）
     */
    @ApiModelProperty(value = "呼叫类型 (0 呼叫业主 1呼叫中心 2呼叫物业 3呼叫转移（云电话）)" )
    private String callType;

    /**
     * 呼叫方
     */
    @ApiModelProperty(value = "呼叫方")
    private String caller;

    /**
     * 呼叫方类型(1-设备；2-客户端)
     */
    @ApiModelProperty(value = "呼叫方类型(1-设备；2-客户端)")
    private String callerType;

    /**
     * 呼叫方名称
     */
    @ApiModelProperty(value = "呼叫方名称")
    private String callerName;

    /**
     * 接听方
     */
    @ApiModelProperty(value = "接听方")
    private String answerer;

    /**
     * 接听方类型(1-设备；2-客户端)
     */
    @ApiModelProperty(value = "接听方类型(1-设备；2-客户端)")
    private String answererType;

    /**
     * 接听方名称
     */
    @ApiModelProperty(value = "接听方名称")
    private String answererName;

    /**
     * 呼叫时间
     */
    @ApiModelProperty(value = "呼叫时间")
    private LocalDateTime callTime;

    /**
     * 通话时长
     */
    @ApiModelProperty(value = "通话时长")
    private Integer callDuration;

    /**
     * 呼叫结果(1-接通；0-未接)
     */
    @ApiModelProperty(value = "呼叫结果(1-接通；0-未接)")
    private String callResponse;

    /**
     * 第一张抓拍图片
     */
    @ApiModelProperty(value = "第一张抓拍图片")
    private String firstPicUrl;

    /**
     * 最后一张抓拍图片
     */
    @ApiModelProperty(value = "最后一张抓拍图片")
    private String lastPicUrl;



    /**
     * 租户id
     */
    @ApiModelProperty(value = "租户id")
    private Integer tenantId;

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
