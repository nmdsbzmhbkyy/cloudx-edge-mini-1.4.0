package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 设备管理-监控设备关联
 *
 * @author 邹宇
 * @date 2021-07-12 11:22:21
 */
@Data
@TableName("project_device_monitor_rel")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "设备管理-监控设备关联")
public class ProjectDeviceMonitorRel extends Model<ProjectDeviceMonitorRel> {
    private static final long serialVersionUID = 1L;

    /**
     * 序列
     */
    @TableId
    @ApiModelProperty(value = "序列，自增")
    private Integer seq;
    /**
     * 设备id
     */
    @ApiModelProperty(value = "设备id")
    private String deviceId;
    /**
     * 时限
     */
    @ApiModelProperty(value = "监控设备id")
    private String monitorDeviceId;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String operator;

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
