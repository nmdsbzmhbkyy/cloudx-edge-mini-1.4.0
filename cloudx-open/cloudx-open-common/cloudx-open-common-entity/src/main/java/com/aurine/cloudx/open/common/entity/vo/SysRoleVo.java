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
 * 系统角色Vo
 *
 * @author : Qiu
 * @date : 2022 04 18 17:43
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "系统角色Vo")
public class SysRoleVo extends OpenBaseVo {

    /**
     * 角色id
     */
    @JsonProperty("roleId")
    @JSONField(name = "roleId")
    @ApiModelProperty("角色id")
    @Null(message = "角色id（roleId）新增时需要为空", groups = {InsertGroup.class})
    @NotNull(message = "角色id（roleId）不能为空", groups = {UpdateGroup.class})
    @Max(value = Integer.MAX_VALUE, message = "角色id（roleId）数值过大")
    private Integer roleId;

    /**
     * 角色名称
     */
    @JsonProperty("roleName")
    @JSONField(name = "roleName")
    @ApiModelProperty("角色名称")
    @Size(max = 64, message = "角色名称（roleName）长度不能超过64")
    private String roleName;

    /**
     * 角色标识
     */
    @JsonProperty("roleCode")
    @JSONField(name = "roleCode")
    @ApiModelProperty("角色标识")
    @Size(max = 64, message = "角色标识（roleCode）长度不能超过64")
    private String roleCode;

    /**
     * 角色描述
     */
    @JsonProperty("roleDesc")
    @JSONField(name = "roleDesc")
    @ApiModelProperty("角色描述")
    @Size(max = 255, message = "角色描述（roleDesc）长度不能超过255")
    private String roleDesc;

    /**
     * 数据权限类型
     */
    @JsonProperty("dsType")
    @JSONField(name = "dsType")
    @ApiModelProperty("数据权限类型")
    @NotBlank(message = "数据权限类型（dsType）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "数据权限类型（dsType）长度不能小于1")
    @Size(max = 1, message = "数据权限类型（dsType）长度不能超过1")
    private String dsType;

    /**
     * 删除标记,1:已删除,0:正常
     */
    @JsonProperty("delFlag")
    @JSONField(name = "delFlag")
    @ApiModelProperty("删除标记,1:已删除,0:正常")
    @NotBlank(message = "删除标记（delFlag）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "删除标记（delFlag）长度不能小于1")
    @Size(max = 1, message = "删除标记（delFlag）长度不能超过1")
    private String delFlag;

    /**
     * 数据权限作用范围
     */
    @JsonProperty("dsScope")
    @JSONField(name = "dsScope")
    @ApiModelProperty("数据权限作用范围")
    @Size(max = 255, message = "数据权限作用范围（dsScope）长度不能超过255")
    private String dsScope;

    /**
     * 部门id
     */
    @JsonProperty("deptId")
    @JSONField(name = "deptId")
    @ApiModelProperty("部门id")
    @Max(value = Integer.MAX_VALUE, message = "部门id（deptId）数值过大")
    private Integer deptId;
}
