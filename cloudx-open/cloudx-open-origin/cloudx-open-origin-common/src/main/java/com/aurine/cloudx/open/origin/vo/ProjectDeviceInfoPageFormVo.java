package com.aurine.cloudx.open.origin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Title: ProjectDeviceInfoPageFormVo
 * Description: 设备管理分页查询表单
 *
 * @author xull@aurine.cn
 * @version 1.0.0
 * @date 2020/5/20 15:13
 */
@Data
@ApiModel("设备管理分页查询表单")
public class ProjectDeviceInfoPageFormVo {
    /**
     * 设备类型id
     */
    @ApiModelProperty("设备类型id")
    private String deviceTypeId;
    /**
     * 项目id
     */
    @ApiModelProperty("项目id")
    private Integer projectId;
    /**
     * 设备名称
     */
    @ApiModelProperty(value = "设备名称")
    private String deviceName;
    /**
     * 设备sn
     */
    @ApiModelProperty(value = "设备sn")
    private String sn;
    /**
     * 设备描述
     */
    @ApiModelProperty(value = "设备描述")
    private String deviceDesc;
    /**
     * 设备编码，可存储第三方编码
     */
    @ApiModelProperty(value = "设备编码，可存储第三方编码")
    private String deviceCode;
    /**
     * 1 在线 2 离线 3 故障 4 未激活 9 未知
     */
    @ApiModelProperty(value = "1 在线 2 离线 3 故障 4 未激活 9 未知")
    private String status;
    /**
     * 设备区域
     */
    @ApiModelProperty(value = "设备区域")
    private String deviceRegionId;

    /**
     * 产品id
     */
    @ApiModelProperty(value = "产品id")
    private String productId;

    /**
     * 设备ID
     */
    @ApiModelProperty(value = "设备ID")
    private String deviceId;

    /**
     * 没有异常 1 ，有异常 0
     */
    @ApiModelProperty(value = "配置状态 没有异常 1 ，有异常 0")
    private String configurationStatus;

    /**
     * 这里存放要排除的设备ID (这些ID的设备不会出现在最后的查询结果中)
     */
    @ApiModelProperty(value = "这里存放要排除的设备ID (这些ID的设备不会出现在最后的查询结果中)")
    private List<String> excludedDeviceIDList;

    /**
     * '接入方式  1 自动 2 手动 9 未定义',
     */
    @ApiModelProperty(value = "'接入方式  1 自动 2 手动 9 未定义',")
    private String accessMethod;
}
