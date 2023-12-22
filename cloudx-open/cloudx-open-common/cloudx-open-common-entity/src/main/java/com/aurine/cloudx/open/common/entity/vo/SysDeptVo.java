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
 * 系统部门Vo
 *
 * @author : Qiu
 * @date : 2022 04 18 17:43
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "系统部门Vo")
public class SysDeptVo extends OpenBaseVo {

    /**
     * 部门id
     */
    @JsonProperty("deptId")
    @JSONField(name = "deptId")
    @ApiModelProperty("部门id")
    @Null(message = "部门id（deptId）新增时需要为空", groups = {InsertGroup.class})
    @NotNull(message = "部门id（deptId）不能为空", groups = {UpdateGroup.class})
    @Max(value = Integer.MAX_VALUE, message = "部门id（deptId）数值过大")
    private Integer deptId;

    /**
     * 部门名称
     */
    @JsonProperty("name")
    @JSONField(name = "name")
    @ApiModelProperty("部门名称")
    @Size(max = 64, message = "部门名称（name）长度不能超过64")
    private String name;

    /**
     * 排序值
     */
    @JsonProperty("sort")
    @JSONField(name = "sort")
    @ApiModelProperty("排序值")
    @NotNull(message = "排序值（sort）不能为空", groups = {InsertGroup.class})
    @Max(value = Integer.MAX_VALUE, message = "排序值（sort）数值过大")
    private Integer sort;

    /**
     * 父级部门id
     */
    @JsonProperty("parentId")
    @JSONField(name = "parentId")
    @ApiModelProperty("父级部门id")
    @Max(value = Integer.MAX_VALUE, message = "父级部门id（parentId）数值过大")
    private Integer parentId;

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
     * 层级类型id
     */
    @JsonProperty("deptTypeId")
    @JSONField(name = "deptTypeId")
    @ApiModelProperty(value = "层级类型id")
    @Size(max = 11, message = "层级类型id（deptTypeId）长度不能超过11")
    private String deptTypeId;

    /**
     * 层级类型名称
     */
    @JsonProperty("deptTypeName")
    @JSONField(name = "deptTypeName")
    @ApiModelProperty(value = "层级类型名称")
    @Size(max = 32, message = "层级类型名称（deptTypeName）长度不能超过32")
    private String deptTypeName;

    /**
     * 是否关联
     */
    @JsonProperty("isAssociated")
    @JSONField(name = "isAssociated")
    @ApiModelProperty(value = "是否关联")
    @SizeCustom(message = "是否关联（isAssociated）长度不能小于1")
    @Size(max = 1, message = "是否关联（isAssociated）长度不能超过1")
    private String isAssociated;
}
