package com.aurine.cloudx.estate.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 住户通知计划发送对象(ProjectNoticePlanTarget)表实体类
 *
 * @author makejava
 * @since 2020-12-15 11:41:37
 */
@Data
@TableName("project_notice_plan_target")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "住户通知计划发送对象(ProjectNoticePlanTarget)")
public class ProjectNoticePlanTarget extends Model<ProjectNoticePlanTarget> {

    private static final long serialVersionUID = -73287834344515793L;



    /**
     * 计划id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "计划id")
    private String planId;


    /**
     * 房屋id
     */
    @ApiModelProperty(value = "房屋id")
    private String houseId;


    /**
     * 最后更新人
     */
    @ApiModelProperty(value = "最后更新人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer operator;


    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;


}