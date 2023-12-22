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
 * 物理策略Vo
 *
 * @author : Qiu
 * @date : 2021 12 16 17:16
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "物理策略Vo")
public class PhysicalPassPolicyVo extends OpenBaseVo {

    /**
     * 序列
     */
    @JsonProperty("seq")
    @JSONField(name = "seq")
    @ApiModelProperty(value = "序列", hidden = true)
    @Null(message = "序列（seq）需要为空")
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
     * 策略id
     */
    @JsonProperty("policyId")
    @JSONField(name = "policyId")
    @ApiModelProperty(value = "策略id")
    @Null(message = "策略id（policyId）新增时需要为空", groups = {InsertGroup.class})
    @NotBlank(message = "策略id（policyId）不能为空", groups = {UpdateGroup.class})
    @Size(max = 32, message = "策略id（policyId）长度不能超过32")
    private String policyId;

    /**
     * 设备id
     */
    @JsonProperty("deviceId")
    @JSONField(name = "deviceId")
    @ApiModelProperty(value = "设备id")
    @NotBlank(message = "设备id（deviceId）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "设备id（deviceId）长度不能小于1")
    @Size(max = 32, message = "设备id（deviceId）长度不能超过32")
    private String deviceId;
}

