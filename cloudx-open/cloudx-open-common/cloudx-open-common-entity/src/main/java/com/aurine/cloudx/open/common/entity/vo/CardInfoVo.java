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
 * 卡信息Vo
 *
 * @author : Qiu
 * @date : 2021 12 20 16:35
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "卡信息Vo")
public class CardInfoVo extends OpenBaseVo {

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
     * 卡id
     */
    @JsonProperty("cardId")
    @JSONField(name = "cardId")
    @ApiModelProperty(value = "卡id")
    @Null(message = "卡id（cardId）新增时需要为空", groups = {InsertGroup.class})
    @NotBlank(message = "卡id（cardId）不能为空", groups = {UpdateGroup.class})
    @Size(max = 32, message = "卡id（cardId）长度不能超过32")
    private String cardId;

    /**
     * 卡号第三方编码
     */
    @JsonProperty("cardCode")
    @JSONField(name = "cardCode")
    @ApiModelProperty(value = "卡号第三方编码")
    @Size(max = 64, message = "卡号第三方编码（cardCode）长度不能超过64")
    private String cardCode;

    /**
     * 卡号
     */
    @JsonProperty("cardNo")
    @JSONField(name = "cardNo")
    @ApiModelProperty(value = "卡号")
    @NotBlank(message = "卡号（cardNo）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "卡号（cardNo）长度不能小于1")
    @Size(max = 32, message = "卡号（cardNo）长度不能超过32")
    private String cardNo;

    /**
     * 人员类型 1 住户 2 员工 3 访客
     */
    @JsonProperty("personType")
    @JSONField(name = "personType")
    @ApiModelProperty(value = "人员类型 1 住户 2 员工 3 访客")
    @Size(max = 1, message = "人员类型（personType）长度不能超过1")
    private String personType;

    /**
     * 人员id，根据人员类型取对应表id。卡未使用时为空
     */
    @JsonProperty("personId")
    @JSONField(name = "personId")
    @ApiModelProperty(value = "人员id，根据人员类型取对应表id。卡未使用时为空")
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
