package com.aurine.cloudx.estate.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 员工培训主表(ProjectTraining)表实体类
 *
 * @author makejava
 * @since 2021-01-13 14:16:51
 */
@Data
@TableName("project_training")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "员工培训主表(ProjectTraining)")
public class ProjectTraining extends Model<ProjectTraining> {

    private static final long serialVersionUID = -82347969177145173L;

    private Integer seq;
    /**
     * 培训id, uuid
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "培训id, uuid")
    private String trainingId;


    /**
     * 主题
     */
    @ApiModelProperty(value = "主题")
    private String title;


    /**
     * 开始日期
     */
    @ApiModelProperty(value = "开始日期")
    private LocalDateTime beginTime;


    /**
     * 截止日期
     */
    @ApiModelProperty(value = "截止日期")
    private LocalDateTime endTime;


    /**
     * 培训说明
     */
    @ApiModelProperty(value = "培训说明")
    private String remark;

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