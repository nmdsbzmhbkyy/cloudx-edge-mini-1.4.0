package com.aurine.cloudx.open.origin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Title: ProjectDeviceInfoPageVo
 * Description: 设备分页查询视图
 *
 * @author xull@aurine.cn
 * @version 1.0.0
 * @date 2020/5/20 15:00
 */
@Data
@ApiModel("设备分页查询视图")
public class ProjectDeviceInfoPageVo {
    /**
     * 设备id, 32位uuid
     */
    @ApiModelProperty(value = "设备id, 32位uuid")
    private String deviceId;
    /**
     * 设备编码，可存储第三方编码
     */
    @ApiModelProperty(value = "设备编码，可存储第三方编码")
    private String deviceCode;

    /**
     * 设备别名（描述）
     */
    @ApiModelProperty(value = "设备别名（描述）")
    private String deviceAlias;

    /**
     * 设备类型
     */
    @ApiModelProperty(value = "设备类型")
    private String deviceType;
    /**
     * 设备类型名称
     */
    @ApiModelProperty("设备类型名称")
    private String deviceTypeName;
    /**
     * 设备类型编码
     */
    @ApiModelProperty("设备类型编码")
    private String deviceTypeCode;
    /**
     * 设备名称
     */
    @ApiModelProperty(value = "设备名称")
    private String deviceName;
    /**
     * 组团
     */
    @ApiModelProperty("组团")
    private String deviceEntityId;
    /**
     * 组团名称
     */
    @ApiModelProperty("组团名称")
    private String deviceEntityName;
    /**
     * 设备区域
     */
    @ApiModelProperty(value = "设备区域")
    private String deviceRegionId;
    /**
     * 设备区域名称
     */
    @ApiModelProperty(value = "设备区域名称")
    private String deviceRegionName;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    /**
     * 1 在线 2 离线 3 故障 4 未激活 9 未知
     */
    @ApiModelProperty(value = "1 在线 2 离线 3 故障 4 未激活 9 未知")
    private String status;

    /**
     * 设备SN
     */
    @ApiModelProperty(value = "设备SN")
    private String sn;

    /**
     * 检查项数量
     */
    @ApiModelProperty(value = "检查项数量")
    private Integer checkItemNum;

    /**
     * 配置状态 正常：true
     */
    @ApiModelProperty(value = "配置状态 正常：true")
    private boolean configurationStatus;

    /**
     * '接入方式  1 自动 2 手动 9 未定义',
     */
    @ApiModelProperty(value = "'接入方式  1 自动 2 手动 9 未定义',")
    private String accessMethod;
}
