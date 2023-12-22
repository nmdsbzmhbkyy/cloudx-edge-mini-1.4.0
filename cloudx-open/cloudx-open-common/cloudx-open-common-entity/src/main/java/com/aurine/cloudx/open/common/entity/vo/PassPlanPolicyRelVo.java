package com.aurine.cloudx.open.common.entity.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.aurine.cloudx.open.common.entity.base.OpenBaseVo;
import com.aurine.cloudx.open.common.validate.annotation.SizeCustom;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;

/**
 * 通行方案策略关系Vo
 *
 * @author : Qiu
 * @date : 2021 12 16 17:16
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "通行方案策略关系Vo")
public class PassPlanPolicyRelVo extends OpenBaseVo {

    /**
     * 序列
     */
    @JsonProperty("seq")
    @JSONField(name = "seq")
    @ApiModelProperty(value = "序列", position = -1)
    @Null(message = "序列（seq）新增时需要为空", groups = {InsertGroup.class})
    @NotNull(message = "序列（seq）不能为空", groups = {UpdateGroup.class})
    @Max(value = Integer.MAX_VALUE, message = "序列（seq）数值过大")
    private Integer seq;

    /**
     * 项目id
     */
    @JsonProperty("projectId")
    @JSONField(name = "projectId")
    @ApiModelProperty(value = "项目id", required = true, position = -2)
    @NotNull(message = "项目id（projectId）不能为空", groups = {InsertGroup.class})
    @Max(value = Integer.MAX_VALUE, message = "项目id（projectId）数值过大")
    private Integer projectId;

    /**
     * 方案id
     */
    @JsonProperty("planId")
    @JSONField(name = "planId")
    @ApiModelProperty(value = "方案id")
    @NotBlank(message = "方案id（planId）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "方案id（planId）长度不能小于1")
    @Size(max = 32, message = "方案id（planId）长度不能超过32")
    private String planId;

    /**
     * 策略id
     */
    @JsonProperty("policyId")
    @JSONField(name = "policyId")
    @ApiModelProperty(value = "策略id")
    @NotBlank(message = "策略id（policyId）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "策略id（policyId）长度不能小于1")
    @Size(max = 32, message = "策略id（policyId）长度不能超过32")
    private String policyId;

    /**
     * 策略类型 1 逻辑策略 2 物理策略
     */
    @JsonProperty("policyType")
    @JSONField(name = "policyType")
    @ApiModelProperty(value = "策略类型 1 逻辑策略 2 物理策略")
    @NotBlank(message = "策略类型（policyType）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "策略类型（policyType）长度不能小于1")
    @Size(max = 1, message = "策略类型（policyType）长度不能超过1")
    private String policyType;
}

