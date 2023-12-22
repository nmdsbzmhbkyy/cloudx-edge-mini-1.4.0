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
import java.time.LocalDateTime;

/**
 * 人员设备权限关系Vo
 *
 * @author : Qiu
 * @date : 2021 12 16 17:16
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "人员设备权限关系Vo")
public class PersonDeviceRelVo extends OpenBaseVo {

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
     * 人员类型 1 住户 2 员工 3 访客
     */
    @JsonProperty("personType")
    @JSONField(name = "personType")
    @ApiModelProperty(value = "人员类型 1 住户 2 员工 3 访客")
    @NotBlank(message = "人员类型（personType）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "人员类型（personType）长度不能小于1")
    @Size(max = 1, message = "人员类型（personType）长度不能超过1")
    private String personType;

    /**
     * 人员id, 根据人员类型取对应表id
     */
    @JsonProperty("personId")
    @JSONField(name = "personId")
    @ApiModelProperty(value = "人员id, 根据人员类型取对应表id")
    @NotBlank(message = "人员id（personId）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "人员id（personId）长度不能小于1")
    @Size(max = 32, message = "人员id（personId）长度不能超过32")
    private String personId;

    /**
     * 通行方案id
     */
    @JsonProperty("planId")
    @JSONField(name = "planId")
    @ApiModelProperty(value = "通行方案id")
    @Size(max = 32, message = "通行方案id（planId）长度不能超过32")
    private String planId;

    /**
     * 设备id
     */
    @JsonProperty("deviceId")
    @JSONField(name = "deviceId")
    @ApiModelProperty(value = "设备id")
    @NotBlank(message = "设备id（deviceId）不能为空", groups = {InsertGroup.class})
    @NotBlank(message = "设备id（deviceId）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "设备id（deviceId）长度不能小于1")
    @Size(max = 32, message = "设备id（deviceId）长度不能超过32")
    private String deviceId;

    /**
     * 状态 1 正常 2 失效
     */
    @JsonProperty("status")
    @JSONField(name = "status")
    @ApiModelProperty(value = "状态 1 正常 2 失效")
    @NotBlank(message = "状态（status）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "状态（status）长度不能小于1")
    @Size(max = 5, message = "状态（status）长度不能超过5")
    private String status;

    /**
     * 是否启用 1 启用 0 禁用。与该方案关联的人员是否处于启用状态
     */
    @JsonProperty("isActive")
    @JSONField(name = "isActive")
    @ApiModelProperty(value = "是否启用 1 启用 0 禁用")
    @NotBlank(message = "是否启用（isActive）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "是否启用（isActive）长度不能小于1")
    @Size(max = 1, message = "是否启用（isActive）长度不能超过1")
    private String isActive;

    /**
     * 生效时间
     */
    @JsonProperty("effTime")
    @JSONField(name = "effTime", format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "生效时间")
    @NotNull(message = "生效时间（effTime）不能为空", groups = {InsertGroup.class})
    private LocalDateTime effTime;

    /**
     * 失效时间
     */
    @JsonProperty("expTime")
    @JSONField(name = "expTime", format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "失效时间")
    private LocalDateTime expTime;
}

