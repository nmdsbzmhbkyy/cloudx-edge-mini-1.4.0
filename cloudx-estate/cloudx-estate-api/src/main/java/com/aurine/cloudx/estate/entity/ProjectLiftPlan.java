package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 电梯通行方案
 */
@Data
@TableName("project_lift_plan")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "电梯通行方案")
public class ProjectLiftPlan extends Model<ProjectLiftPlan> {


    /**
     * 方案id,uuid, 一个方案内可配置多个设备
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value="方案id,uuid, 一个方案内可配置多个设备")
    private String planId;
    /**
     * 方案名称
     */
    @ApiModelProperty(value="方案名称")
    private String planName;
    /**
     * 方案对象 1 住户 2 员工 3 访客
     */
    @ApiModelProperty(value="方案对象 1 住户 2 员工 3 访客")
    private String planObject;

    /**
     * 是否默认 1 是 0 否
     */
    @ApiModelProperty(value="是否默认 1 是 0 否")
    private String isDefault;


    /**
     * 操作人
     */
    @ApiModelProperty(value="操作人")
    @TableField(fill = FieldFill.INSERT)
    private Integer operator;
    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value="更新时间")
    private LocalDateTime updateTime;


}
