package com.aurine.cloudx.dashboard.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 项目楼栋人员统计
 *
 * @ClassName: ProjectBuildingPersonTotalMView
 * @author : Qiu <qiujb@miligc.com>
 * @date : 2021 08 16 9:40
 * @Copyright:
 */

@Data
@TableName("PROJECT_BUILDING_PERSON_TOTAL_MVIEW")
public class ProjectBuildingPersonTotalMView extends BaseDashboardEntity {

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
     * 人员（住户）数量
     */
    @ApiModelProperty("人员（住户）数量")
    @TableField("CNT_TOTAL")
    private Long cntTotal;

    /**
     * 访客数量
     */
    @ApiModelProperty("访客数量")
    @TableField("CNT_VISITOR")
    private Long cntVisitor;

    /**
     * 男性数量
     */
    @ApiModelProperty("男性数量")
    @TableField("CNT_MALE")
    private Long cntMale;

    /**
     * 女性数量
     */
    @ApiModelProperty("女性数量")
    @TableField("CNT_FEMALE")
    private Long cntFemale;

    /**
     * 未成年数量
     */
    @ApiModelProperty("未成年数量")
    @TableField("CNT_CHILDREN")
    private Long cntChildren;

    /**
     * 成年人数量
     */
    @ApiModelProperty("成年人数量")
    @TableField("CNT_ADULT")
    private Long cntAdult;

    /**
     * 老年人数量
     */
    @ApiModelProperty("老年人数量")
    @TableField("CNT_OLD")
    private Long cntOld;
}
