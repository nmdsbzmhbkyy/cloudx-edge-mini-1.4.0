package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
public class ProjectDeviceCallEvent extends OpenBasePo<ProjectDeviceCallEvent> {
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
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    private Integer projectId;
}
