

package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * <p>物理策略 PO</p>
 * @ClassName: ProjectPhysicalPassPolicy
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/20 15:45
 * @Copyright:
 */
@Data
@TableName("project_physical_pass_policy")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "物理策略")
public class ProjectPhysicalPassPolicy extends Model<ProjectPhysicalPassPolicy> {
private static final long serialVersionUID = 1L;


    /**
     * 策略id,uuid, 一个方案内可配置多个设备
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value="策略id,uuid, 一个方案内可配置多个设备")
    private String policyId;
    /**
     * 设备id
     */
    @ApiModelProperty(value="设备id")
    private String deviceId;

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
