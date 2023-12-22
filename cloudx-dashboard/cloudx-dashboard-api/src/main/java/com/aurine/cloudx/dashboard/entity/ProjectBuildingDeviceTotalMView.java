package com.aurine.cloudx.dashboard.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 项目楼栋设备统计
 *
 * @ClassName: ProjectBuildingDeviceTotalMView
 * @author : Qiu <qiujb@miligc.com>
 * @date : 2021 08 18 15:08
 * @Copyright:
 */

@Data
@TableName("PROJECT_BUILDING_DEVICE_TOTAL_MVIEW")
public class ProjectBuildingDeviceTotalMView extends BaseDashboardEntity {

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
     * 总数
     */
    @ApiModelProperty("总数")
    @TableField("CNT_TOTAL")
    private Long cntTotal;

    /**
     * 在线数
     */
    @ApiModelProperty("在线数")
    @TableField("CNT_ONLINE")
    private Long cntOnline;

    /**
     * 离线数
     */
    @ApiModelProperty("离线数")
    @TableField("CNT_OFFLINE")
    private Long cntOffline;

    /**
     * 故障数
     */
    @ApiModelProperty("故障数")
    @TableField("CNT_FAULT")
    private Long cntFault;

    /**
     * 未激活数
     */
    @ApiModelProperty("未激活数")
    @TableField("CNT_NOT_ACTIVE")
    private Long cntNotActive;

    /**
     * 未激活数
     */
    @ApiModelProperty("未激活数")
    @TableField("CNT_UNKNOWN")
    private Long cntUnknown;
}
