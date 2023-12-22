
package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * <p>电梯策略方案关联PO </p>
 * @ClassName: ProjectLiftPlanPolicyRel
 * @author: 陈喆 <chenz@aurine.cn>
 * @date: 2022/2/14 11:48
 * @Copyright:
 */
@Data
@TableName("project_lift_plan_policy_rel")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "电梯策略方案关联")
public class ProjectLiftPlanPolicyRel extends Model<ProjectLiftPlanPolicyRel> {
private static final long serialVersionUID = 1L;

    /**
     * 方案id,uuid, 一个方案内可配置多个设备
     */
    @ApiModelProperty(value="方案id,uuid, 一个方案内可配置多个设备")
    private String planId;
    /**
     * 策略id
     */
    @ApiModelProperty(value="策略id")
    private String policyId;
    /**
     * 策略类型 1 逻辑策略 2 物理策略
     */
    @ApiModelProperty(value="策略类型 1 逻辑策略 2 物理策略")
    private String policyType;

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
