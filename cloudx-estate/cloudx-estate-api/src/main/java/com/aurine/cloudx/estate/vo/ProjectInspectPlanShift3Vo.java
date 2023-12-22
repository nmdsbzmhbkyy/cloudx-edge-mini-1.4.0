package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 设备巡检计划班次信息（自定义）(ProjectInspectPlanShift3)表实体类
 *
 * @author 王良俊
 * @since 2020-07-23 18:34:26
 */
@Data
@ApiModel(value = "设备巡检计划班次信息vo对象")
public class ProjectInspectPlanShift3Vo {

    /**
     * 开始时间和结束时间范围
     */
    @ApiModelProperty(value = "开始时间和结束时间范围")
    private String[] timeRange;

    /**
     * 执行人员
     */
    @ApiModelProperty(value = "执行人员")
    private String[] staffIdArr;

}