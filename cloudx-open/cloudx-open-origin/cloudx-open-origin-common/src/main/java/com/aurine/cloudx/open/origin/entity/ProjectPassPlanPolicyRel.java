
package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
public class ProjectPassPlanPolicyRel extends OpenBasePo<ProjectPassPlanPolicyRel> {
private static final long serialVersionUID = 1L;

    /**
     * 序列
     */
    @TableId
    @ApiModelProperty(value = "序列", hidden = true)
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
    }
