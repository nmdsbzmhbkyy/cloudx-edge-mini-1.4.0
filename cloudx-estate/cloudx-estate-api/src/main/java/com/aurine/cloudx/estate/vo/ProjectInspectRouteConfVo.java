package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectInspectRouteConf;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 设备巡检路线设置(ProjectInspectRouteConfVo)对象
 *
 * @author 王良俊
 * @since 2020-07-24 17:16:18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "设备巡检路线vo对象")
public class ProjectInspectRouteConfVo extends ProjectInspectRouteConf {

    /**
     * 制定人员
     */
    @ApiModelProperty(value = "制定人员")
    private String operatorName;

    /**
     * 巡检点数
     */
    @ApiModelProperty(value = "巡检点数")
    private String pointNum;

    /**
     * 巡检点id数组
     */
    @ApiModelProperty(value = "巡检点id数组")
    private String[] pointIdArr;

}