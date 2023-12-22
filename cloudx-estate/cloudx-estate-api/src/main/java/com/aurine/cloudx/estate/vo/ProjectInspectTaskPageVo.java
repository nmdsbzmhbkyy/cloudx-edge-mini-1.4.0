package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectInspectTask;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * (ProjectInspectTaskPageVo)设备巡检任务分页Vo
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/10/27 8:39
 */
@Data
@ApiModel("设备巡检任务分页Vo")
public class ProjectInspectTaskPageVo extends ProjectInspectTask {

    /**
     * 计划巡检人员ID列表
     */
    @ApiModelProperty(value = "计划巡检人员ID列表")
    private String planStaffIds;
    /**
     * 实际巡检人员ID列表
     */
    @ApiModelProperty(value = "实际巡检人员ID列表")
    private String execStaffIds;

    /**
     * 完成时间
     */
    @ApiModelProperty(value = "完成时间")
    private String doneTime;


}
