

package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * <p>电梯逻辑策略PO</p>
 * @ClassName: ProjectLogicLiftPolicy
 * @author:  陈喆<chenz@aurine.cn>
 * @date: 2022/2/22 15:37
 * @Copyright:
 */
@Data
@TableName("project_logic_lift_policy")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "电梯逻辑策略")
public class ProjectLogicLiftPolicy extends Model<ProjectLogicLiftPolicy> {
private static final long serialVersionUID = 1L;


    /**
     * 策略id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value="策略id")
    private String policyId;

    /**
     * 宏类型，如isLocalLadder
     */
    @ApiModelProperty(value="宏类型，如isLocalLadder")
    private String macroId;

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
