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
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;

/**
 * 开放平台推送订阅回调信息Vo
 * 注：该Vo对象是应用层级的Vo对象，所以在校验规则上引入了AppGroup（应用分组）
 * 注：应用层级的Vo对象新增时需要使用InsertOnlyGroup（只新增分组），防止校验到默认规则，修改则使用UpdateOnlyGroup（只修改分组）
 *
 * @author : Qiu
 * @date : 2021 12 09 11:28
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "开放平台推送订阅回调信息Vo")
public class OpenPushSubscribeCallbackVo extends OpenBaseVo {

    /**
     * 序列
     */
    @JsonProperty("seq")
    @JSONField(name = "seq")
    @ApiModelProperty(value = "序列", hidden = true)
    @Null(message = "序列（seq）需要为空", groups = {Default.class, AppGroup.class})
    private Integer seq;

    /**
     * 回调id，uuid
     */
    @JsonProperty("callbackId")
    @JSONField(name = "callbackId")
    @ApiModelProperty(value = "回调id，uuid")
    @Null(message = "回调id（callbackId）新增时需要为空", groups = {InsertOnlyGroup.class})
    @NotBlank(message = "回调id（callbackId）不能为空", groups = {UpdateOnlyGroup.class})
    @Size(max = 32, message = "回调id（callbackId）长度不能超过32", groups = {Default.class, AppGroup.class})
    private String callbackId;

    /**
     * 回调类型，0-配置类；1-级联入云类；2-操作类；3-指令类；4-事件类；5-反馈类；9-其他
     */
    @JsonProperty("callbackType")
    @JSONField(name = "callbackType")
    @ApiModelProperty(value = "回调类型，0-配置类；1-级联入云类；2-操作类；3-指令类；4-事件类；5-反馈类；9-其他", required = true, position = -2)
    @NotBlank(message = "回调类型（callbackType）不能为空", groups = {InsertOnlyGroup.class})
    @SizeCustom(message = "回调类型（callbackType）长度不能小于1", groups = {Default.class, AppGroup.class})
    @Size(max = 1, message = "回调类型（callbackType）长度不能超过1", groups = {Default.class, AppGroup.class})
    private String callbackType;

    /**
     * 回调方式，0-url方式；1-topic方式，默认为0
     */
    @JsonProperty("callbackMode")
    @JSONField(name = "callbackMode")
    @ApiModelProperty(value = "回调方式，0-url方式；1-topic方式，默认为0", required = true, position = -2)
    @NotBlank(message = "回调方式（callbackMode）不能为空", groups = {InsertOnlyGroup.class})
    @SizeCustom(message = "回调方式（callbackMode）长度不能小于1", groups = {Default.class, AppGroup.class})
    @Size(max = 1, message = "回调方式（callbackMode）长度不能超过1", groups = {Default.class, AppGroup.class})
    private String callbackMode;

    /**
     * 回调地址
     */
    @JsonProperty("callbackUrl")
    @JSONField(name = "callbackUrl")
    @ApiModelProperty(value = "回调地址", required = true, position = -3)
    @SizeCustom(message = "回调地址（callbackUrl）长度不能小于1", groups = {Default.class, AppGroup.class})
    @Size(max = 1024, message = "回调地址（callbackUrl）长度不能超过1024", groups = {Default.class, AppGroup.class})
    private String callbackUrl;

    /**
     * 回调主题
     */
    @JsonProperty("callbackTopic")
    @JSONField(name = "callbackTopic")
    @ApiModelProperty(value = "回调主题", required = true, position = -3)
    @SizeCustom(message = "回调主题（callbackTopic）长度不能小于1", groups = {Default.class, AppGroup.class})
    @Size(max = 255, message = "回调主题（callbackTopic）长度不能超过255", groups = {Default.class, AppGroup.class})
    private String callbackTopic;

    /**
     * 回调请求头参数，参数名为Param（预留字段）
     */
    @JsonProperty("callbackHeaderParam")
    @JSONField(name = "callbackHeaderParam")
    @ApiModelProperty(value = "回调请求头参数，参数名为Param")
    @SizeCustom(message = "回调请求头参数（callbackHeaderParam）长度不能小于1", groups = {Default.class, AppGroup.class})
    private String callbackHeaderParam;

    /**
     * 回调说明
     */
    @JsonProperty("callbackDesc")
    @JSONField(name = "callbackDesc")
    @ApiModelProperty(value = "回调说明")
    @Size(max = 255, message = "回调说明（callbackDesc）长度不能超过255", groups = {Default.class, AppGroup.class})
    private String callbackDesc;

    /**
     * 项目UUID；如果回调类型是配置类可为空
     */
    @JsonProperty("projectUUID")
    @JSONField(name = "projectUUID")
    @ApiModelProperty(value = "项目UUID；如果回调类型是配置类可为空", required = true, position = -1)
    @SizeCustom(message = "项目UUID（projectUUID）长度不能小于1", groups = {Default.class, AppGroup.class})
    @Size(max = 32, message = "项目UUID（projectUUID）长度不能超过32", groups = {Default.class, AppGroup.class})
    private String projectUUID;

    /**
     * 应用ID
     */
    @JsonProperty("appId")
    @JSONField(name = "appId")
    @ApiModelProperty(value = "应用ID", hidden = true)
    @Null(message = "应用ID（appId）需要为空", groups = {Default.class, AppGroup.class})
    private String appId;

    /**
     * 项目类型，0-云平台端；1-边缘网关；2-WR2X（预留字段）
     */
    @JsonProperty("projectType")
    @JSONField(name = "projectType")
    @ApiModelProperty(value = "项目类型，0-云平台端；1-边缘网关；2-WR2X（预留字段）")
    @Size(max = 5, message = "项目类型（projectType）长度不能超过5", groups = {Default.class, AppGroup.class})
    private String projectType;

}
