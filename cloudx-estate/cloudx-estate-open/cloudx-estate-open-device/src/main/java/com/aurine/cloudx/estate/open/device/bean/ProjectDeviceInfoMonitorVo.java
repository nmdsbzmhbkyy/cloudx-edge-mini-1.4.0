package com.aurine.cloudx.estate.open.device.bean;

import com.aurine.cloudx.estate.vo.ProjectDeviceInfoVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * Title: ProjectDeviceInfoVo
 * Description:
 *
 * @author xull@aurine.cn
 * @version 1.0.0
 * @date 2020/5/21 14:22
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("区口机信息视图")
public class ProjectDeviceInfoMonitorVo extends ProjectDeviceInfoVo {

    /**
     * 设备SN
     */
    @ApiModelProperty(value = "设备SN")
    @NotBlank(message = "设备SN不能为空")
    private String sn;

    @ApiModelProperty(value = "设备名称")
    @NotBlank(message = "设备名称不能为空")
    private String deviceName;

    /**
     * 设备别名
     */
    @ApiModelProperty(value = "设备别名")
    private String deviceAlias;

    /**
     * 设备编码
     */
    @ApiModelProperty(value = "设备编号")
    private String deviceCode;
    /**
     * ipv4地址
     */
    @ApiModelProperty(value = "ipv4地址")
    private String ipv4;
    /**
     * ipv6地址
     */
    @ApiModelProperty(value = "ipv6地址")
    private String ipv6;

    /**
     * 设备端口
     */
    @ApiModelProperty(value = "网络端口号")
    @NotBlank(message = "网络端口号不能为空")
    private Integer port;

    /**
     * 品牌型号
     */
    @ApiModelProperty("品牌型号")
    @NotBlank(message = "品牌型号不能为空")
    private String brand;


}
