package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * (ProjectDeviceCameraVo)
 * 监控设备
 *
 * @author xull
 * @since 2020/7/8 10:17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("监控设备对象")
public class ProjectDeviceCameraVo extends ProjectDeviceInfoVo {
    /**
     * 所属设备
     */
    @ApiModelProperty("所属设备")
    private String belongToDeviceId;
    /**
     * 所属设备名称
     */
    @ApiModelProperty("所属设备名称")
    private String belongToDeviceName;

    /**
     * 所属设备编码
     */
    @ApiModelProperty("所属设备编码")
    private String belongToDeviceCode;
    /**
     * 监控类型
     */
    @ApiModelProperty("监控类型")
    private String monitorType;

    /**
     * 检查项数量
     */
    @ApiModelProperty(value = "检查项数量")
    private Integer checkItemNum;

    /**
     * 空间ID
     */
    @ApiModelProperty("空间ID")
    private String corpId;

    /**
     * 国标ID
     */
    @ApiModelProperty("国标ID")
    private String gbId;

    /**
     * 服务区ID
     */
    @ApiModelProperty("服务区ID")
    private String regionId;
}
