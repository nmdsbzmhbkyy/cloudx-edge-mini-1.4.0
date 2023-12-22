package com.aurine.cloudx.estate.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 培训员工表(ProjectTrainingStaff)表实体类
 *
 * @author makejava
 * @since 2021-01-13 14:34:26
 */
@Data
@TableName("project_training_staff")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "培训员工表(ProjectTrainingStaff)")
public class ProjectTrainingStaff extends Model<ProjectTrainingStaff> {

    private static final long serialVersionUID = 127885723507934201L;

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
     * 员工id，关联project_staff.staffId
     */
    @ApiModelProperty(value = "员工id，关联project_staff.staffId")
    private String staffId;

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