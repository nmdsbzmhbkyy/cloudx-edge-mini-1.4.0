package com.aurine.cloudx.estate.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Title: OpenApiProjectDeviceInfoManageDto
 * Description: 设备管理变更通知dto
 *
 * @author 顾文豪
 * @version 1.0.0
 * @date 2023/11/14 10:00
 */
@Data
@ApiModel("设备管理变更通知dto")
public class OpenApiProjectDeviceInfoManageDto {
    @ApiModelProperty("操作类型，1 新增 2 修改 3 删除")
    private String operateType;
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
     * 设备名称
     */
    @ApiModelProperty(value = "设备名称")
    private String deviceName;
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
     * 设备MAC
     */
    @ApiModelProperty(value = "设备MAC")
    private String mac;

    /**
     * 楼栋id
     */
    @ApiModelProperty(value = "楼栋id")
    private String buildingId;
    /**
     * 单元id
     */
    @ApiModelProperty(value = "单元id")
    private String unitId;
    /**
     * 房屋id
     */
    @ApiModelProperty(value = "房屋id")
    private String houseId;

    /**
     * 楼栋名称
     */
    @ApiModelProperty(value = "楼栋名称")
    private String buildingNo;
    /**
     * 单元名称
     */
    @ApiModelProperty(value = "单元名称")
    private String unitNo;
    /**
     * 房号
     */
    @ApiModelProperty(value = "房号")
    private String houseCode;

    /**
     * 出入类型 0 出 1 入
     */
    @ApiModelProperty(value = "出入类型 0 出 1 入")
    private String passType;


    /**
     * ipv4地址
     */
    @ApiModelProperty(value = "ipv4地址")
    private String ipv4;

    /**
     * 设备端口
     */
    @ApiModelProperty(value = "设备端口")
    @TableField("dPort")
    private Integer port;

//	/**
//	 * 是否配置 1=已配置 0=未配置
//	 */
//	@ApiModelProperty(value = "是否配置 1=已配置 0=未配置")
//	private String configured;
    /**
     * 项目UUID
     */
    @ApiModelProperty(value="项目UUID")
    private String projectUUID;

    @ApiModelProperty(value = "项目ID")
    private Integer projectId;

}
