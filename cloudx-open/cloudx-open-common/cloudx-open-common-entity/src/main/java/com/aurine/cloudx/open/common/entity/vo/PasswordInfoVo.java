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
 * 密码信息Vo
 *
 * @author : Qiu
 * @date : 2021 12 20 16:35
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "密码信息Vo")
public class PasswordInfoVo extends OpenBaseVo {

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
    @ApiModelProperty(value = "项目id", required = true, position = -1)
    @NotNull(message = "项目id（projectId）不能为空", groups = {InsertGroup.class})
    @Max(value = Integer.MAX_VALUE, message = "项目id（projectId）数值过大")
    private Integer projectId;

    /**
     * 密码id
     */
    @JsonProperty("passId")
    @JSONField(name = "passId")
    @ApiModelProperty(value = "密码id")
    @Null(message = "密码id（passId）新增时需要为空", groups = {InsertGroup.class})
    @NotBlank(message = "密码id（passId）不能为空", groups = {UpdateGroup.class})
    @Size(max = 32, message = "密码id（passId）长度不能超过32")
    private String passId;

    /**
     * 密码第三方ID
     */
    @JsonProperty("passCode")
    @JSONField(name = "passCode")
    @ApiModelProperty(value = "密码第三方ID")
    @Size(max = 64, message = "密码第三方ID（passCode）长度不能超过64")
    private String passCode;

    /**
     * 密码
     */
    @JsonProperty("passwd")
    @JSONField(name = "passwd")
    @ApiModelProperty(value = "密码")
    @NotBlank(message = "密码（passwd）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "密码（passwd）长度不能小于1")
    @Size(max = 32, message = "密码（passwd）长度不能超过32")
    private String passwd;

    /**
     * 人员类型 1 住户 2 员工 3 访客
     */
    @JsonProperty("personType")
    @JSONField(name = "personType")
    @ApiModelProperty(value = "人员类型 1 住户 2 员工 3 访客")
    @Size(max = 1, message = "人员类型（personType）长度不能超过1")
    private String personType;

    /**
     * 人员id，根据人员类型取对应表id。
     */
    @JsonProperty("personId")
    @JSONField(name = "personId")
    @ApiModelProperty(value = "人员id，根据人员类型取对应表id。")
    @Size(max = 32, message = "人员id（personId）长度不能超过32")
    private String personId;

    /**
     * 状态 0 未使用 1 使用中 2 冻结
     */
    @JsonProperty("status")
    @JSONField(name = "status")
    @ApiModelProperty(value = "状态 0 未使用 1 使用中 2 冻结")
    @NotBlank(message = "状态（status）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "状态（status）长度不能小于1")
    @Size(max = 5, message = "状态（status）长度不能超过5")
    private String status;
}
