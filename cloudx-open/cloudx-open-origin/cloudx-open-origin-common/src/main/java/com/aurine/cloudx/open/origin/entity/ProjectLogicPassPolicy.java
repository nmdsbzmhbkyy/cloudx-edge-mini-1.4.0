

package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>逻辑策略PO</p>
 * @ClassName: ProjectLogicPassPolicy
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/20 15:37
 * @Copyright:
 */
@Data
@TableName("project_logic_pass_policy")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "逻辑策略")
public class ProjectLogicPassPolicy extends OpenBasePo<ProjectLogicPassPolicy> {
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
    }
