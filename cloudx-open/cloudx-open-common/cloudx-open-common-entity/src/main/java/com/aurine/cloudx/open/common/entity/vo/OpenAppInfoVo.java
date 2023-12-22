package com.aurine.cloudx.open.common.entity.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.aurine.cloudx.open.common.entity.base.OpenBaseVo;
import com.aurine.cloudx.open.common.validate.annotation.SizeCustom;
import com.aurine.cloudx.open.common.validate.group.AppGroup;
import com.aurine.cloudx.open.common.validate.group.InsertOnlyGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateOnlyGroup;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;

/**
 * 开放平台应用信息Vo
 * 注：该Vo对象是应用层级的Vo对象，所以在校验规则上引入了AppGroup（应用分组）
 * 注：应用层级的Vo对象新增时需要使用InsertOnlyGroup（只新增分组），防止校验到默认规则，修改则使用UpdateOnlyGroup（只修改分组）
 *
 * @author : Qiu
 * @date : 2021 12 09 10:49
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "开放平台应用信息Vo")
public class OpenAppInfoVo extends OpenBaseVo {

    /**
     * 序列
     */
    @JsonProperty("seq")
    @JSONField(name = "seq")
    @ApiModelProperty(value = "序列", hidden = true)
    @Null(message = "序列（seq）需要为空")
    private Integer seq;

    /**
     * 应用UUID，uuid，标识内部唯一
     */
    @JsonProperty("appUUID")
    @JSONField(name = "appUUID")
    @ApiModelProperty(value = "应用UUID，uuid，标识内部唯一")
    @Null(message = "应用UUID（appUUID）新增时需要为空", groups = {InsertOnlyGroup.class})
    @NotBlank(message = "应用UUID（appUUID）不能为空", groups = {UpdateOnlyGroup.class})
    @Size(max = 32, message = "应用UUID（appUUID）长度不能超过32", groups = {Default.class, AppGroup.class})
    private String appUUID;

    /**
     * 应用id，对外提供的应用id
     */
    @JsonProperty("appId")
    @JSONField(name = "appId")
    @ApiModelProperty(value = "应用id，对外提供的应用id", required = true, position = -3)
    @NotNull(message = "应用id（appId）不能为空", groups = {InsertOnlyGroup.class})
    @SizeCustom(message = "应用id（appId）长度不能小于1", groups = {Default.class, AppGroup.class})
    @Size(max = 32, message = "应用id（appId）长度不能超过32", groups = {Default.class, AppGroup.class})
    private String appId;

    /**
     * 应用名称
     */
    @JsonProperty("appName")
    @JSONField(name = "appName")
    @ApiModelProperty(value = "应用名称", required = true, position = -2)
    @NotBlank(message = "应用名称（appName）不能为空", groups = {InsertOnlyGroup.class})
    @SizeCustom(message = "应用名称（appName）长度不能小于1", groups = {Default.class, AppGroup.class})
    @Size(max = 255, message = "应用名称（appName）长度不能超过255", groups = {Default.class, AppGroup.class})
    private String appName;

    /**
     * 应用描述
     */
    @JsonProperty("appDesc")
    @JSONField(name = "appDesc")
    @ApiModelProperty(value = "应用描述", position = -2)
    @Size(max = 1024, message = "应用描述（appDesc）长度不能超过255", groups = {Default.class, AppGroup.class})
    private String appDesc;

    /**
     * 应用类型
     */
    @JsonProperty("appType")
    @JSONField(name = "appType")
    @ApiModelProperty(value = "应用类型")
    @Size(max = 5, message = "应用类型（appType）长度不能超过5", groups = {Default.class, AppGroup.class})
    private String appType;

}
