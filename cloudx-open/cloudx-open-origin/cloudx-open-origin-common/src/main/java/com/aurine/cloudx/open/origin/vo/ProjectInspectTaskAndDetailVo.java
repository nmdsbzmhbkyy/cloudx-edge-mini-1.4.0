package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectInspectTask;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * (ProjectInspectTaskDetailVo)
 * 巡检任务及明细列表
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/10/26 11:45
 */
@Data
@ApiModel("projectInspectTaskDetail")
public class ProjectInspectTaskAndDetailVo extends ProjectInspectTask {
    @ApiModelProperty("巡检任务明细")
    private List<ProjectInspectTaskDetailVo> projectInspectTaskDetails;

    /**
     * 计划巡检人id，逗号分隔
     */
    @ApiModelProperty(value = "计划巡检人Id，逗号分隔")
    private String planStaffIds;


    /**
     * 实际执行人员id，逗号分隔
     */
    @ApiModelProperty(value = "实际执行人员，逗号分隔")
    private String execStaffIds;
}
