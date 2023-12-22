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
 * 组团配置Vo
 *
 * @author : Qiu
 * @date : 2021 12 16 17:16
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "组团配置Vo")
public class EntityLevelCfgVo extends OpenBaseVo {

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
    @ApiModelProperty(value = "项目id", required = true, position = -1)
    @NotNull(message = "项目id（projectId）不能为空", groups = {InsertGroup.class})
    @Max(value = Integer.MAX_VALUE, message = "项目id（projectId）数值过大")
    private Integer projectId;

    /**
     * 层级编号
     */
    @JsonProperty("level")
    @JSONField(name = "level")
    @ApiModelProperty(value = "层级编号")
    @NotNull(message = "层级编号（level）不能为空", groups = {InsertGroup.class})
    @Max(value = Integer.MAX_VALUE, message = "层级编号（level）数值过大")
    private Integer level;

    /**
     * 用于和第三方对接，编号位数
     */
    @JsonProperty("codeRule")
    @JSONField(name = "codeRule")
    @ApiModelProperty(value = "用于和第三方对接，编号位数")
    @Max(value = Integer.MAX_VALUE, message = "编号位数（codeRule）数值过大")
    private Integer codeRule;

    /**
     * 层级描述
     */
    @JsonProperty("levelDesc")
    @JSONField(name = "levelDesc")
    @ApiModelProperty(value = "层级描述")
    @NotBlank(message = "层级描述（levelDesc）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "层级描述（levelDesc）长度不能小于1")
    @Size(max = 64, message = "层级描述（levelDesc）长度不能超过64")
    private String levelDesc;

    /**
     * 是否启用
     */
    @JsonProperty("isDisable")
    @JSONField(name = "isDisable")
    @ApiModelProperty(value = "是否启用 0：启动，1关闭")
    @NotBlank(message = "是否启用（isDisable）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "是否启用（isDisable）长度不能小于1")
    @Size(max = 5, message = "是否启用（isDisable）长度不能超过5")
    private String isDisable;
}

