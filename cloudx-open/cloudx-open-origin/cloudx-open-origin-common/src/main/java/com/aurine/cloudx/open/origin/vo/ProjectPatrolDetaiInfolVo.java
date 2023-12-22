package com.aurine.cloudx.open.origin.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: ProjectPatrolDetaiInfolVo
 * @author: 王良俊 <>
 * @date:  2020年09月22日 上午09:23:28
 * @Copyright:
*/
@Data
public class ProjectPatrolDetaiInfolVo {

    @ApiModelProperty("未巡数")
    Integer unpatrolNum;

    @ApiModelProperty("已巡数")
    Integer patrolNum;

    @ApiModelProperty("巡更结果正常数")
    Integer normalNum;

    @ApiModelProperty("巡更结果异常数")
    Integer unNormalNum;

    @ApiModelProperty("签到正常数")
    Integer signNormalNum;

    @ApiModelProperty("巡更结果异常数")
    Integer signUnNormalNum;

    @ApiModelProperty("巡更人员分配列表")
    List<ProjectPatrolDetailVo> patrolDetailVoList = new ArrayList<>();
}