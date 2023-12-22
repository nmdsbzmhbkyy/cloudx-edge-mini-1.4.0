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
 * 框架信息Vo
 *
 * @author : Qiu
 * @date : 2021 12 16 17:16
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "框架信息Vo")
public class FrameInfoVo extends OpenBaseVo {

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
     * 实体id
     */
    @JsonProperty("entityId")
    @JSONField(name = "entityId")
    @ApiModelProperty(value = "实体id")
    @NotBlank(message = "实体id（entityId）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "实体id（entityId）长度不能小于1")
    @Size(max = 32, message = "实体id（entityId）长度不能超过32")
    private String entityId;

    /**
     * 实体编码
     */
    @JsonProperty("entityCode")
    @JSONField(name = "entityCode")
    @ApiModelProperty(value = "实体编码")
    @Size(max = 64, message = "实体编码（entityCode）长度不能超过64")
    private String entityCode;

    /**
     * 框架编码
     */
    @JsonProperty("frameNo")
    @JSONField(name = "frameNo")
    @ApiModelProperty(value = "框架编码")
    @Size(max = 20, message = "框架编码（frameNo）长度不能超过20")
    private String frameNo;

    /**
     * 实体名称
     */
    @JsonProperty("entityName")
    @JSONField(name = "entityName")
    @ApiModelProperty(value = "实体名称")
    @NotBlank(message = "实体名称（entityName）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "实体名称（entityName）长度不能小于1")
    @Size(max = 32, message = "实体名称（entityName）长度不能超过32")
    private String entityName;

    /**
     * 上级id
     */
    @JsonProperty("puid")
    @JSONField(name = "puid")
    @ApiModelProperty(value = "上级id")
    @Size(max = 32, message = "上级id（puid）长度不能超过32")
    private String puid;

    /**
     * 是否楼栋
     */
    @JsonProperty("isBuilding")
    @JSONField(name = "isBuilding")
    @ApiModelProperty(value = "是否楼栋")
    @Size(max = 1, message = "是否楼栋（isBuilding）长度不能超过1")
    private String isBuilding;

    /**
     * 是否单元
     */
    @JsonProperty("isUnit")
    @JSONField(name = "isUnit")
    @ApiModelProperty(value = "是否单元")
    @Size(max = 1, message = "是否单元（isUnit）长度不能超过1")
    private String isUnit;

    /**
     * 是否房屋
     */
    @JsonProperty("isHouse")
    @JSONField(name = "isHouse")
    @ApiModelProperty(value = "是否房屋")
    @Size(max = 1, message = "是否房屋（isHouse）长度不能超过1")
    private String isHouse;

    /**
     * 层级
     */
    @JsonProperty("level")
    @JSONField(name = "level")
    @ApiModelProperty(value = "层级")
    @Max(value = Integer.MAX_VALUE, message = "层级（level）数值过大")
    private Integer level;
}

