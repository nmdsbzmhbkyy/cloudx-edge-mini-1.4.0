package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.*;
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
public class ProjectTrainingStaff extends OpenBasePo<ProjectTrainingStaff> {

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


}