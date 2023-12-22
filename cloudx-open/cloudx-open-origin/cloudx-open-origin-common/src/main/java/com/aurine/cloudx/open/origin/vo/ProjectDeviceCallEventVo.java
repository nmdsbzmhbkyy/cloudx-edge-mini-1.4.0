package com.aurine.cloudx.open.origin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 项目呼叫事件(ProjectDeviceCallEvent)Vo
 *
 * @author : Qiu
 * @date : 2020 12 15 17:26
 */
@Data
@ApiModel("项目呼叫事件视图")
public class ProjectDeviceCallEventVo {

    /**
     * 呼叫方名称
     */
    @ApiModelProperty(value = "呼叫方名称")
    private String callerName;

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
     * 呼叫开始时间，查询条件
     */
    @ApiModelProperty(value = "呼叫开始时间，查询条件")
    private LocalDateTime callTimeBegin;

    /**
     * 呼叫结束时间，查询条件
     */
    @ApiModelProperty(value = "呼叫开始时间，查询条件")
    private LocalDateTime callTimeEnd;

    /**
     * 员工id，查询条件
     */
    @ApiModelProperty(value = "员工id，查询条件")
    private String staffId;

    /**
     * 呼叫方类型(1-设备；2-客户端)，查询条件
     */
    @ApiModelProperty(value = "呼叫方类型(1-设备；2-客户端)，查询条件")
    private String callerType;

    /**
     * 接听方类型(1-设备；2-客户端)，查询条件
     */
    @ApiModelProperty(value = "接听方类型(1-设备；2-客户端)，查询条件")
    private String answererType;

    /**
     * 项目id，查询条件
     */
    @ApiModelProperty(value = "项目id，查询条件")
    private Integer projectId;
}
