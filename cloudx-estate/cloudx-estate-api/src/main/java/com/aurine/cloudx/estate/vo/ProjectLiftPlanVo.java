

package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>电梯通行方案VO</p>
 *
 * @ClassName: ProjectLiftPlan
 * @author: 陈喆 <chenz@aurine.cn>
 * @date: 2022/2/22 14:44
 * @Copyright:
 */
@Data
@ApiModel(value = "电梯通行方案")
public class ProjectLiftPlanVo {
    private static final long serialVersionUID = 1L;

    /**
     * 方案id,uuid, 一个方案内可配置多个设备
     */
    @ApiModelProperty(value = "方案id,uuid, 一个方案内可配置多个设备")
    private String planId;
    /**
     * 方案名称
     */
    @ApiModelProperty(value = "方案名称")
    private String planName;
    /**
     * 方案对象 1 住户 2 员工 3 访客
     */
    @ApiModelProperty(value = "方案对象 1 住户 2 员工 3 访客")
    private String planObject;

    /**
     * 是否默认 1 是 0 否
     */
    @ApiModelProperty(value = "是否默认 1 是 0 否")
    private String isDefault;


    /****************逻辑策略*******************/

    /**
     * 宏类型
     */
    @ApiModelProperty(value = "宏类型")
    private String[] macroIdArray;

/***************物理策略**********************/

    /**
     * 楼层
     */
    @ApiModelProperty(value = "楼层")
    private String[] floorArray;

    /**
     * 项目id
     */
    @ApiModelProperty("项目id")
    private Integer projectId;

    /**
     * 租户ID
     */
    @ApiModelProperty(value = "租户ID", hidden = true)
    private Integer tenantId;

}
