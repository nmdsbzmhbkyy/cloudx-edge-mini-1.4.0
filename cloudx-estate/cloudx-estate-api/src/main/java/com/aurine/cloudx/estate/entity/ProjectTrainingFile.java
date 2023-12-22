package com.aurine.cloudx.estate.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;

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
public class ProjectTrainingFile extends Model<ProjectTrainingFile> {

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

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;


    /**
     * 最后操作人
     */
    @ApiModelProperty(value = "最后操作人")
    @TableField(fill = FieldFill.INSERT)
    private Integer operator;


}