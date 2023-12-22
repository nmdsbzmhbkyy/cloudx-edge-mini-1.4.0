package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectPatrolDetail;
import com.aurine.cloudx.estate.entity.ProjectPatrolPerson;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: ProjectPatrolDetailVo
 * @author: 王良俊 <>
 * @date:  2020年09月17日 上午11:30:28
 * @Copyright:
*/
@Data
public class ProjectPatrolDetailVo extends ProjectPatrolDetail {

    @ApiModelProperty("巡更人员分配列表")
    List<ProjectPatrolPersonVo> patrolPersonVoList = new ArrayList<>();
}