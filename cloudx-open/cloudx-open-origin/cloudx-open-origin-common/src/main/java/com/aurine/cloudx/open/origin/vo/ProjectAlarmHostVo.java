package com.aurine.cloudx.open.origin.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * (ProjectAlarmHostVo)
 * 报警主机
 *
 * @author 邹宇
 * @since 2021-6-9 17:12:00
 */

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("报警主机对象")
public class ProjectAlarmHostVo extends ProjectDeviceInfoVo {

    /**
     * 设备编码
     */
    @ApiModelProperty("设备编码")
    private String deviceCode;

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
    private String deviceRegionId;

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

    /**
     * 执行状态
     */
    @ApiModelProperty("执行状态")
    private String armedStatus;

    /**
     * 防区数
     */
    @ApiModelProperty("防区数")
    private String defenceAreaNum;

}
