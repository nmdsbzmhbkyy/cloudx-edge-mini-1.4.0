package com.aurine.cloudx.open.origin.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 设备管理-监控设备关联
 *
 * @author 邹宇
 * @date 2021-07-12 11:22:21
 */
@Data
public class ProjectDeviceMonitorRelVo {
    /**
     * 设备id
     */
    @ApiModelProperty("设备id")
    private String deviceId;

    /**
     * 监控设备id
     */
    @ApiModelProperty("监控设备id")
    private String[] monitorDeviceId;

}
