

package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
public class ProjectPhysicalPassPolicy extends OpenBasePo<ProjectPhysicalPassPolicy> {
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
    }
