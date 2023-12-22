package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectDeviceModifyLog;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 设备修改记录Vo
 *
 * @author 邹宇
 * @date 2021-9-26 16:05:25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("设备修改记录Vo")
public class ProjectDeviceModifyLogVo extends ProjectDeviceModifyLog {
    /**
     * 设备名称
     */
    @ApiModelProperty(value = "设备名称")
    private String deviceName;

    /**
     * ipv4
     */
    @ApiModelProperty(value = "ipv4")
    private String ipv4;

    /**
     * 设备编号
     */
    @ApiModelProperty(value = "设备编号")
    private String deviceCode;

    /**
     * mac
     */
    @ApiModelProperty(value = "mac")
    private String mac;




}
