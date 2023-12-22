package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 周界报警表
 *
 * @author 邹宇
 * @date 2021-6-8 14:30:55
 */
@Data
@TableName("project_perimeter_alarm_event")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "周界报警表")
public  class ProjectPerimeterAlarmEvent extends OpenBasePo<ProjectPerimeterAlarmEvent> {

    private static final long serialVersionUID = -95687272445932901L;

    /**
     * 序列，自增
     */
    @TableId
    @ApiModelProperty(value = "序列，自增")
    private Integer seq;

    /**
     * 事件id，uuid
     */
    @ApiModelProperty(value = "事件id")
    private String eventId;
    /**
     * 报警主机设备id
     */
    @ApiModelProperty(value = "报警主机设备id")
    private String deviceId;

    /**
     * 区域名称
     */
    @ApiModelProperty(value = "区域名称")
    private String regionName;

    /**
     * 报警主机设备名称
     */
    @ApiModelProperty(value = "报警主机设备名称")
    private String deviceName;
    /**
     * 防区名
     */
    @ApiModelProperty(value = "防区名")
    private String channelName;

    /**
     * 防区号
     */
    @ApiModelProperty(value = "防区号")
    private String channelId;
    /**
     * 报警模块设备号
     */
    @ApiModelProperty(value = "报警模块设备号")
    private String moduleNo;

    /**
     * 报警类型 0 其他告警 1入侵告警 2 触网告警  3 短路告警 4 断路告警 5 防拆告警 6 故障告警
     */
    @ApiModelProperty(value = "报警类型")
    private String alaramType;

    /**
     * 报警时间
     */
    @ApiModelProperty(value = "报警时间")
    private Date alaramTime;

    /**
     * 状态 0 未处理 1 已处理
     */
    @ApiModelProperty(value = "状态")
    private String execStatus;


}
