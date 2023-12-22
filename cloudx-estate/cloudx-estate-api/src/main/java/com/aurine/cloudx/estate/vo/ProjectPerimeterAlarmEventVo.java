package com.aurine.cloudx.estate.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 周界报警
 *
 * @author 邹宇
 * @date 2021-6-8 14:30:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectPerimeterAlarmEventVo {


    private Integer current;
    private Integer size;


    /**
     *
     */
    @ApiModelProperty(value = "报警类型 0 其他告警 1入侵告警 2 触网告警  3 短路告警 4 断路告警 5 防拆告警 6 故障告警")
    private String alaramType;

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态 0 未处理 1 已处理'")
    private String execStatus;

    /**
     * 开始时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "开始时间")
    private LocalDate alarmTimeBegin;

    /**
     * 结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "结束时间")
    private LocalDate alarmTimeEnd;


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
     * 报警时间
     */
    @ApiModelProperty(value = "报警时间")
    private Date alaramTime;

    @ApiModelProperty(value = "操作人")
    private Integer operator;

    /**
     * 操作时间
     */
    @ApiModelProperty(value = "操作时间")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    /**
     * 处理人
     */

    private String name;

    /**
     * 处理人
     */
    @ApiModelProperty(value = "处理人userId")
    private String deviceRegionId;

    /**
     * app排序状态
     */
    @ApiModelProperty(value = "app排序状态")
    private  String processType;


    /**
     * rtsp流地址
     */
    @ApiModelProperty(value = "rtsp流地址")
    private String rtspUrl;

    /**
     * 区域id
     */
    @ApiModelProperty(value = "账号")
    private String userName;

    /**
     * 区域id
     */
    @ApiModelProperty(value = "密码")
    private String password;
}
