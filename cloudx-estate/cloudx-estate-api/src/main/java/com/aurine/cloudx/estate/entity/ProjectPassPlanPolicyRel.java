
package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * <p>策略方案关联PO </p>
 * @ClassName: ProjectPassPlanPolicyRel
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/20 15:54
 * @Copyright:
 */
@Data
@TableName("project_pass_plan_policy_rel")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "策略方案关联")
public class ProjectPassPlanPolicyRel extends Model<ProjectPassPlanPolicyRel> {
private static final long serialVersionUID = 1L;

    /**
     * seq
     */
    @TableId
    @ApiModelProperty(value="自增长id")
    private Integer seq;

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
