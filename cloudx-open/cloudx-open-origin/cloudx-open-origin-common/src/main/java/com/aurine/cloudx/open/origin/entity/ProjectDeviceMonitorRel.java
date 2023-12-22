package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
public class ProjectDeviceMonitorRel extends OpenBasePo<ProjectDeviceMonitorRel> {
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
}
