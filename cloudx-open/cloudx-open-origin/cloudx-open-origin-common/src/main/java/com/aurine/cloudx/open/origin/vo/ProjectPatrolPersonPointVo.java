package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectPatrolPersonPoint;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * (ProjectPatrolPersonPointVo)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/10/29 14:22
 */
@Data
public class ProjectPatrolPersonPointVo extends ProjectPatrolPersonPoint {

    /**
     * 巡更记录id
     */
    @ApiModelProperty(value = "巡更记录id")
    private String patrolId;
    /**
     * 巡更点id
     */
    @ApiModelProperty(value = "巡更点id")
    private String pointId;
    /**
     * 巡更点位置
     */
    @ApiModelProperty(value = "巡更点位置")
    private String pointName;
    /**
     * 巡更人
     */
    @ApiModelProperty(value = "巡更人")
    private String staffId;
    /**
     * 巡更人名称
     */
    @ApiModelProperty(value = "巡更人名称")
    private String staffName;

}
