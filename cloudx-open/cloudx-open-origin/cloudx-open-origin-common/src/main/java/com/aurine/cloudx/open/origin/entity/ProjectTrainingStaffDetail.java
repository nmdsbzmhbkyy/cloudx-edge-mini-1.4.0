package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 员工培训明细表(ProjectTrainingStaffDetail)表实体类
 *
 * @author makejava
 * @since 2021-01-14 08:37:39
 */
@Data
@TableName("project_training_staff_detail")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "员工培训明细表(ProjectTrainingStaffDetail)")
public class ProjectTrainingStaffDetail extends OpenBasePo<ProjectTrainingStaffDetail> {

    private static final long serialVersionUID = 696254136842334378L;

    /**
     * 明细id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "明细id")
    private String detailId;


    /**
     * 培训id, uuid
     */
    @ApiModelProperty(value = "培训id, uuid", required = true)
    private String trainingId;


    /**
     * 员工id
     */
    @ApiModelProperty(value = "员工id", required = true)
    private String staffId;


    /**
     * 资料id
     */
    @ApiModelProperty(value = "资料id", required = true)
    private String fileId;


    /**
     * 观看进度 1 已观看 0 未观看
     */
    @ApiModelProperty(value = "观看进度 1 已观看 0 未观看")
    private String progress;


    /**
     * 完成日期
     */
    @ApiModelProperty(value = "完成日期")
    private LocalDateTime completeTime;


}