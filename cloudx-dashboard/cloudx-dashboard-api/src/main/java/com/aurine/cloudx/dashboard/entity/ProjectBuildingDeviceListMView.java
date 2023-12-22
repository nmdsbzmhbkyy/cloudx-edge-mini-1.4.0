package com.aurine.cloudx.dashboard.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 项目楼栋设备列表
 *
 * @ClassName: ProjectBuildingDeviceListMView
 * @author : Qiu <qiujb@miligc.com>
 * @date : 2021 08 18 17:02
 * @Copyright:
 */

@Data
@TableName("PROJECT_BUILDING_DEVICE_LIST_MVIEW")
public class ProjectBuildingDeviceListMView extends BaseDashboardEntity {

    /**
     * 楼栋ID
     */
    @ApiModelProperty("楼栋ID")
    @TableField("BUILDINGID")
    private String buildingId;

    /**
     * 楼栋名称
     */
    @ApiModelProperty("楼栋名称")
    @TableField("BUILDINGNAME")
    private String buildingName;

    /**
     * 楼层
     */
    @ApiModelProperty("楼层")
    @TableField("STOREY")
    private Integer storey;

    /**
     * 房屋ID
     */
    @ApiModelProperty("房屋ID")
    @TableField("HOUSEID")
    private String houseId;

    /**
     * 设备ID
     */
    @ApiModelProperty("设备ID")
    @TableField("DEVICEID")
    private String deviceId;

    /**
     * 设备名称
     */
    @ApiModelProperty("设备名称")
    @TableField("DEVICENAME")
    private String deviceName;

    /**
     * 设备编码
     */
    @ApiModelProperty("设备编码")
    @TableField("DEVICECODE")
    private String deviceCode;

    /**
     * 设备类型
     */
    @ApiModelProperty("设备类型")
    @TableField("DEVICETYPE")
    private String deviceType;

    /**
     * 设备类型
     */
    @ApiModelProperty("设备类型")
    @TableField("DEVICETYPENAME")
    private String deviceTypeName;

    /**
     * 设备状态
     */
    @ApiModelProperty("设备状态")
    @TableField("DSTATUS")
    private String dstatus;

    /**
     * 是否启用
     */
    @ApiModelProperty("是否启用")
    @TableField("ISACTIVE")
    private String isActive;

    /**
     * 单元ID
     */
    @ApiModelProperty("单元ID")
    @TableField("UNITID")
    private String unitId;

    /**
     * 设备SN
     */
    @ApiModelProperty("设备SN")
    @TableField("SN")
    private String sn;

    /**
     * 设备Mac
     */
    @ApiModelProperty("设备Mac")
    @TableField("MAC")
    private String mac;

    /**
     * 设备区域ID
     */
    @ApiModelProperty("设备区域ID")
    @TableField("DEVICEREGIONID")
    private String deviceRegionId;

    /**
     * 设备区域
     */
    @ApiModelProperty("设备区域")
    @TableField("DEVICEREGIONNAME")
    private String deviceRegionName;

    /**
     * 设备安装位置代码
     */
    @ApiModelProperty("设备安装位置代码")
    @TableField("LOCATECODE")
    private String locateCode;

    /**
     * 设备安装位置
     */
    @ApiModelProperty("设备安装位置")
    @TableField("LOCATION")
    private String location;
}
