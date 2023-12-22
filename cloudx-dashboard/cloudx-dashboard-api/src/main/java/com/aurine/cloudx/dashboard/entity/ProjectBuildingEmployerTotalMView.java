package com.aurine.cloudx.dashboard.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 项目楼栋实有单位统计
 *
 * @ClassName: ProjectBuildingHouseTotalMView
 * @author : Qiu <qiujb@miligc.com>
 * @date : 2021 08 17 10:04
 * @Copyright:
 */

@Data
@TableName("PROJECT_BUILDING_EMPLOYER_TOTAL_MVIEW")
public class ProjectBuildingEmployerTotalMView extends BaseDashboardEntity {

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
     * 私有类型经济
     */
    @ApiModelProperty("私有类型经济")
    @TableField("CNT_PRIVATE")
    private Long cntPrivate;

    /**
     * 集体全资类型经济
     */
    @ApiModelProperty("集体全资类型经济")
    @TableField("CNT_GROUP")
    private Long cntGroup;

    /**
     * 其他类型经济
     */
    @ApiModelProperty("其他类型经济")
    @TableField("CNT_Other")
    private Long cntOther;
}
