package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectTraining;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * (ProjectTrainingFormVo) 培训设置表单vo
 *
 * @author guhl
 * @since 2021/1/13 10:16
 */
@Data
@ApiModel("培训设置表单vo")
public class ProjectTrainingFormVo extends ProjectTraining {
    /**
     * 参与员工id集合
     */
    @ApiModelProperty("参与员工id集合")
    List<String> staffIds;
    /**
     * 培训资料id集合
     */
    @ApiModelProperty("培训资料id集合")
    List<String> fileIds;
    /**
     * 员工已读资料数
     */
    @ApiModelProperty("员工已读资料数")
    Integer doneCount;

}