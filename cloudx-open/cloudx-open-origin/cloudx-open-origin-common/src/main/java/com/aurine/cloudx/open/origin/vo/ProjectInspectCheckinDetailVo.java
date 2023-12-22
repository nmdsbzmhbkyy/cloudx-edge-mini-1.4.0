package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectInspectCheckinDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 巡检点签到明细(ProjectInspectCheckinDetail)表实体类
 *
 * @author 王良俊
 * @since 2020-08-04 10:08:51
 */
@Data
@ApiModel(value = "巡检点签到明细Vo对象(ProjectInspectCheckinDetail)")
public class ProjectInspectCheckinDetailVo extends ProjectInspectCheckinDetail {

    @ApiModelProperty(value = "巡检任务ID")
    private String taskId;


}