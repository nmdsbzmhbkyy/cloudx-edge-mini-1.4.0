package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 培训资料表(ProjectTrainingFile)表实体类
 *
 * @author makejava
 * @since 2021-01-13 14:29:07
 */
@Data
@TableName("project_training_file")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "培训资料表(ProjectTrainingFile)")
public class ProjectTrainingFile extends OpenBasePo<ProjectTrainingFile> {

    private static final long serialVersionUID = -77970547942002533L;

    /**
     * uuid
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "uuid")
    private String id;


    /**
     * 培训id，关联project_training.trainingId
     */
    @ApiModelProperty(value = "培训id，关联project_training.trainingId")
    private String trainingId;


    /**
     * 资料id，关联project_training_file_db.fileId
     */
    @ApiModelProperty(value = "资料id，关联project_training_file_db.fileId")
    private String fileId;


}