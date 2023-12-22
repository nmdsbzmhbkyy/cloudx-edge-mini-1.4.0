package com.aurine.cloudx.open.common.entity.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.aurine.cloudx.open.common.entity.base.OpenBaseModel;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Open推送模型
 *
 * @author : Qiu
 * @date : 2021 12 16 17:11
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Open推送模型")
public class OpenPushModel<T> extends OpenBaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 租户ID
     */
    @JsonProperty("tenantId")
    @JSONField(name = "tenantId")
    @ApiModelProperty(value = "租户ID", required = true, position = -9)
    @NotNull(message = "租户ID（tenantId）不能为空")
    @Max(value = Integer.MAX_VALUE, message = "租户ID（tenantId）数值过大")
    private Integer tenantId;

    /**
     * 项目UUID
     */
    @JsonProperty("projectUUID")
    @JSONField(name = "projectUUID")
    @ApiModelProperty(value = "项目UUID", required = true, position = -8)
    @NotBlank(message = "项目UUID（projectUUID）不能为空")
    @Size(max = 64, message = "项目UUID（projectUUID）长度不能超过64")
    private String projectUUID;

    /**
     * 服务类型：
     * config：0-配置类；
     * cascade: 1-级联入云类
     * operate：2-操作类；
     * command：3-指令类；
     * event：4-事件类；
     * feedback：5-反馈类；
     * other：9-其他；
     */
    @JsonProperty("serviceType")
    @JSONField(name = "serviceType")
    @ApiModelProperty(value = "服务类型", required = true, position = -7)
    @NotBlank(message = "服务类型（serviceType）不能为空")
    @Size(max = 32, message = "服务类型（serviceType）长度不能超过32")
    private String serviceType;

    /**
     * 服务名称
     */
    @JsonProperty("serviceName")
    @JSONField(name = "serviceName")
    @ApiModelProperty(value = "服务名称", required = true, position = -6)
    @NotBlank(message = "服务名称（serviceName）不能为空")
    @Size(max = 64, message = "服务名称（serviceName）长度不能超过64")
    private String serviceName;

    /**
     * 级联入云类型
     * apply：0-申请级联入云；
     * revoke：1-撤销级联入云；
     * accept：2-同意级联入云；
     * reject：3-拒绝级联入云；
     */
    @JsonProperty("cascadeType")
    @JSONField(name = "cascadeType")
    @ApiModelProperty(value = "级联入云类型", position = -5)
    @Size(max = 32, message = "级联入云类型（cascadeType）长度不能超过32")
    private String cascadeType;

    /**
     * 操作类型
     * add：0-添加；
     * update：1-修改；
     * delete：2-删除；
     * sync : 3-同步指令
     */
    @JsonProperty("operateType")
    @JSONField(name = "operateType")
    @ApiModelProperty(value = "操作类型", position = -4)
    @Size(max = 32, message = "操作类型（operateType）长度不能超过32")
    private String operateType;

    /**
     * 指令类型
     * close：0-关闭指令；
     * open：1-打开指令；
     * change：2-改变指令；
     * empty :4-清空指令
     */
    @JsonProperty("commandType")
    @JSONField(name = "commandType")
    @ApiModelProperty(value = "指令类型", position = -3)
    @Size(max = 32, message = "指令类型（commandType）长度不能超过32")
    private String commandType;

    /**
     * 实体ID
     * 实体对象的ID
     */
    @JsonProperty("entityId")
    @JSONField(name = "entityId")
    @ApiModelProperty(value = "实体ID", position = -2)
    @Size(max = 32, message = "实体ID（entityId）长度不能超过32")
    private String entityId;

    /**
     * 对象信息
     */
    @Valid
    @JsonProperty("data")
    @JSONField(name = "data")
    @ApiModelProperty(value = "对象信息", required = true, position = -1)
    @NotNull(message = "对象信息（data）不能为空")
    private T data;
}
