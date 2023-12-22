

package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>通行方案VO</p>
 *
 * @ClassName: ProjectPassPlan
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/20 15:17
 * @Copyright:
 */
@Data
@ApiModel(value = "通行方案")
public class ProjectPassPlanVo {
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
     * 宏类型，如isLocalLadder
     */
    @ApiModelProperty(value = "宏类型，如isLocalLadder")
    private String[] macroIdArray;

/***************物理策略**********************/

    /**
     * 设备id
     */
    @ApiModelProperty(value = "设备id")
    private String[] deviceIdArray;

    /**
     * 设备状态列表
     */
    @ApiModelProperty(value = "设备状态列表")
    private List<ProjectPassDeviceVo> deviceList;

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
