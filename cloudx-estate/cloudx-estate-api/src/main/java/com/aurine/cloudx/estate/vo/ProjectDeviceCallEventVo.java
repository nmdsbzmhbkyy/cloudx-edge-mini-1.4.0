package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.Api;
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
     * 是否隐私数据（隐私数据：室内机呼叫业主,业主呼叫室内机）
     */
    @ApiModelProperty(value = "是否隐私数据 0否 1是")
    private String isPrivacy;
}
