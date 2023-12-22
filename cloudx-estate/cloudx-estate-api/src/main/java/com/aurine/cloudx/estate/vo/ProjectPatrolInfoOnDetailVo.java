package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectPatrolInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * (ProjectPatrolInfoOnDetailVo)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/10/29 16:22
 */
@Data
@ApiModel("巡更信息及签到明细列表")
public class ProjectPatrolInfoOnDetailVo extends ProjectPatrolInfo {
    @ApiModelProperty("签到明细列表")
    List<ProjectPatrolPersonPointVo> pointVos;
}
