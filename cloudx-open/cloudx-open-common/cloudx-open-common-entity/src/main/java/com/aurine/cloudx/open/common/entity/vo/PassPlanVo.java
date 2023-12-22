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
 * 通行方案Vo
 *
 * @author : Qiu
 * @date : 2021 12 16 17:16
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "通行方案Vo")
public class PassPlanVo extends OpenBaseVo {

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
     * planId,uuid, 一个方案内可配置多个设备
     */
    @JsonProperty("planId")
    @JSONField(name = "planId")
    @ApiModelProperty(value = "通行方案id")
    @Null(message = "通行方案id（planId）新增时需要为空", groups = {InsertGroup.class})
    @NotBlank(message = "通行方案id（planId）不能为空", groups = {UpdateGroup.class})
    @Size(max = 32, message = "通行方案id（planId）长度不能超过32")
    private String planId;

    /**
     * 方案名称
     */
    @JsonProperty("planName")
    @JSONField(name = "planName")
    @ApiModelProperty(value = "方案名称")
    @NotBlank(message = "方案名称（planName）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "方案名称（planName）长度不能小于1")
    @Size(max = 64, message = "方案名称（planName）长度不能超过64")
    private String planName;

    /**
     * 方案对象 1 住户 2 员工 3 访客
     */
    @JsonProperty("planObject")
    @JSONField(name = "planObject")
    @ApiModelProperty(value = "方案对象 1 住户 2 员工 3 访客")
    @NotBlank(message = "方案对象（planObject）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "方案对象（planObject）长度不能小于1")
    @Size(max = 1, message = "方案对象（planObject）长度不能超过1")
    private String planObject;

    /**
     * 楼栋id
     */
    @JsonProperty("buildingId")
    @JSONField(name = "buildingId")
    @ApiModelProperty(value="楼栋id")
    @Size(max = 32, message = "楼栋id（buildingId）长度不能超过32")
    private String buildingId;

    /**
     * 单元id
     */
    @JsonProperty("unitId")
    @JSONField(name = "unitId")
    @ApiModelProperty(value = "单元id")
    @Size(max = 32, message = "单元id（unitId）长度不能超过32")
    private String unitId;

    /**
     * 是否默认 1 是 0 否
     */
    @JsonProperty("isDefault")
    @JSONField(name = "isDefault")
    @ApiModelProperty(value = "是否默认 1 是 0 否")
    @NotBlank(message = "是否默认（isDefault）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "是否默认（isDefault）长度不能小于1")
    @Size(max = 1, message = "是否默认（isDefault）长度不能超过1")
    private String isDefault;
}

