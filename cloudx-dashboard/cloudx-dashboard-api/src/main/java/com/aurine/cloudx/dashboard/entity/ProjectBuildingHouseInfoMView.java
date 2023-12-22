package com.aurine.cloudx.dashboard.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 项目楼栋房屋信息
 *
 * @ClassName: ProjectBuildingHouseInfoMView
 * @author : Qiu <qiujb@miligc.com>
 * @date : 2021 08 17 8:54
 * @Copyright:
 */

@Data
@TableName("PROJECT_BUILDING_HOUSE_INFO_MVIEW")
public class ProjectBuildingHouseInfoMView extends BaseDashboardEntity {

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
     * 单元数
     */
    @ApiModelProperty("单元数")
    @TableField("CNT_UNIT")
    private Long cntUnit;

    /**
     * 房屋数
     */
    @ApiModelProperty("房屋数")
    @TableField("CNT_HOUSE")
    private Long cntHouse;

    /**
     * 人口数
     */
    @ApiModelProperty("人口数")
    @TableField("CNT_PERSON")
    private Long cntPerson;

    /**
     * 面积
     */
    @ApiModelProperty("面积")
    @TableField("CNT_AREA")
    private Long cntArea;
}
