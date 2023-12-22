package com.aurine.cloudx.open.origin.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 *
 * (ProjectSmartWaterMeterVo)
 *
 *
 * @author 邹宇
 * @since 2021-7-5 16:10:00
 */

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("设备vo对象")
public class ProjectDeviceVo extends ProjectDeviceInfoVo {

    /**
     * 设备SN
     */
    @ApiModelProperty("设备SN")
    private String sn;

    /**
     * 设备类型
     */
    @ApiModelProperty("设备类型")
    private String deviceType;

    /**
     * 设备名称
     */
    @ApiModelProperty("设备名称")
    private String deviceName;

    /**
     * 设备描述
     */
    @ApiModelProperty("设备描述")
    private String deviceDesc;

    /**
     * 设备区域
     */
    @ApiModelProperty("设备区域")
    private String deviceRegionName;

    /**
     * 设备区域
     */
    @ApiModelProperty("抄表时间")
    private String meterReadingTime;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 状态 1 在线 2 离线 3 故障 4 未激活 9 未知
     */
    @ApiModelProperty("状态")
    private String status;

}
