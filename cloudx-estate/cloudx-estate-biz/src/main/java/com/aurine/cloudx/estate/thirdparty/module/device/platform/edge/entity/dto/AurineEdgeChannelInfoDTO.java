package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Auther: 黄健杰 <huangjianjie@aurine.cn>
 * @Date: 2022-01-24 09:54:29
 * @Description: AI盒子通道对象DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AurineEdgeChannelInfoDTO {
    /**
     * 父设备ID
     */
    private String devPId;
    /**
     * 通道ID 设备的唯一标识，采用UUID
     */
    private  String channelId;
    /**
     * 通道名称
     */
    private String name;
    /**
     * 通道类型 默认：RTSP
     */
    private String model;
    /**
     * 设备名称（描述）
     */
    private String description;
    /**
     * 地址
     */
    private String url;
    /**
     * 品牌型号
     */
    private String brandModel;
    /**
     * 状态
     */
    private String online;
    /**
     * 启用聚集报警 0:关闭，1:启用； 默认：0
     */
    private Integer enableGatherAlarm;
    /**
     * 报警人数 默认：10
     */
    private Integer alarmNum;
    /**
     * 持续时间 默认60秒，单位：秒
     */
    private Integer durationTime;
    /**
     * 事件上报间隔 单位：秒
     */
    private Integer interval;
    /**
     * 报警星期 格式：[SUNDAY, MONDAY, TUESDAY]
     */
    private String weeks;
    /**
     * 报警时段 时间格式： [00:00-23:59, 00:00-23:59
     */
    private String timePeriod;

    private String password;

    private String username;
}
