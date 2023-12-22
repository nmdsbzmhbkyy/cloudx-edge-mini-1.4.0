

package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 报警事件
 *
 * @author 黄阳光
 * @date 2020-06-04 08:30:07
 */
@Data
@TableName("project_entrance_alarm_event")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "报警事件")
public class ProjectEntranceAlarmEvent extends OpenBasePo<ProjectEntranceAlarmEvent> {
    private static final long serialVersionUID = 1L;

    /**
     * 事件id，uuid
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "事件id，uuid")
    private String eventId;
    /**
     * 人员类型
     */
    @ApiModelProperty(value = "人员类型")
    private String personType;
    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    private String personName;
    /**
     * 设备id
     */
    @ApiModelProperty(value = "设备id")
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
     * 事件描述
     */
    @ApiModelProperty(value = "事件描述")
    private String eventDesc;
    /**
     * 事件时间
     */
    @ApiModelProperty(value = "事件时间")
    private LocalDateTime eventTime;
    /**
     * 抓拍图片
     */
    @ApiModelProperty(value = "抓拍图片")
    private String picUrl;
    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    private String status;

    /**
     * 项目id
     */
    @ApiModelProperty("项目id")
    private Integer projectId;
    /**
     * 事件类型id
     */
    @ApiModelProperty(value = "事件类型id")
    private String eventTypeId;

    /**
     * 报警级别
     */
    @ApiModelProperty(value = "报警级别")
    private String eventLevel;

    /**
     * 设备类型名称
     */
    @ApiModelProperty(value = "设备类型名称")
    private String deviceTypeName;

    /**
     * 事件Id 由中台传入
     */
    @ApiModelProperty(value = "事件流水ID")
    private Integer eventCode;

    /**
     * 事件Id 由中台传入
     */
    @ApiModelProperty(value = "防区号")
    private Integer areaNo;
}
