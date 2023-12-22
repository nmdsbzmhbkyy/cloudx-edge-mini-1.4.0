package com.aurine.cloudx.dashboard.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 项目楼栋房屋统计
 *
 * @ClassName: ProjectBuildingHouseTotalMView
 * @author : Qiu <qiujb@miligc.com>
 * @date : 2021 08 17 9:32
 * @Copyright:
 */

@Data
@TableName("PROJECT_BUILDING_HOUSE_TOTAL_MVIEW")
public class ProjectBuildingHouseTotalMView extends BaseDashboardEntity {

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
     * 业主数
     */
    @ApiModelProperty("业主数")
    @TableField("CNT_OWNER")
    private Long cntOwner;

    /**
     * 业主数
     */
    @ApiModelProperty("家属数")
    @TableField("CNT_FAMILY")
    private Long cntFamily;

    /**
     * 租客数
     */
    @ApiModelProperty("租客数")
    @TableField("CNT_RENT")
    private Long cntRent;

    /**
     * 房间总数
     */
    @ApiModelProperty("房间总数")
    @TableField("CNT_HOUSE_TOTAL")
    private Long cntHouseTotal;

    /**
     * 出租屋数
     */
    @ApiModelProperty("出租屋数")
    @TableField("CNT_HOUSE_RENT")
    private Long cntHouseRent;

    /**
     * 自住房数
     */
    @ApiModelProperty("自住房数")
    @TableField("CNT_HOUSE_SELF")
    private Long cntHouseSelf;

    /**
     * 空置房数
     */
    @ApiModelProperty("空置房数")
    @TableField("CNT_HOUSE_IDLE")
    private Long cntHouseIdle;
}
