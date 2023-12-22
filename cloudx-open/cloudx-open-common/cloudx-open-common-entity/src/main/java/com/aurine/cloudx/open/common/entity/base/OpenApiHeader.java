package com.aurine.cloudx.open.common.entity.base;

import com.alibaba.fastjson.annotation.JSONField;
import com.aurine.cloudx.open.common.validate.group.AppGroup;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import javax.validation.groups.Default;
import java.io.Serializable;

/**
 * OpenApi请求头
 * 注：应用层级接口不需要校验projectUUID、tenantId、projectId，所以在注解处排除
 *
 * @author : Qiu
 * @date : 2022 01 07 16:21
 */

@Data
@ApiModel(value = "OpenApi请求头")
public class OpenApiHeader implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 应用ID
     */
    @JsonProperty("appId")
    @JSONField(name = "appId")
    @ApiModelProperty(value = "应用ID", required = true, position = -3)
    @NotBlank(message = "应用ID（appId）不能为空")
    @Size(max = 32, message = "应用ID（appId）长度不能超过32")
    private String appId;

    /**
     * 项目UUID
     */
    @JsonProperty("projectUUID")
    @JSONField(name = "projectUUID")
    @ApiModelProperty(value = "项目UUID", required = true, position = -2)
    @Null(message = "项目UUID（projectUUID）需要为空", groups = {AppGroup.class})
    @NotBlank(message = "项目UUID（projectUUID）不能为空")
    @Size(max = 64, message = "项目UUID（projectUUID）长度不能超过64")
    private String projectUUID;

    /**
     * 租户ID
     */
    @JsonProperty("tenantId")
    @JSONField(name = "tenantId")
    @ApiModelProperty(value = "租户ID", required = true, position = -1)
    @Null(message = "租户ID（tenantId）需要为空", groups = {AppGroup.class})
    @NotNull(message = "租户ID（tenantId）不能为空")
    @Max(value = Integer.MAX_VALUE, message = "租户ID（tenantId）数值过大")
    private Integer tenantId;

    /**
     * 项目ID
     */
    @JsonProperty("projectId")
    @JSONField(name = "projectId")
    @ApiModelProperty(value = "项目ID", hidden = true)
    @Null(message = "项目ID（projectId）需要为空", groups = {Default.class, AppGroup.class})
    @Max(value = Integer.MAX_VALUE, message = "项目ID（projectId）数值过大")
    private Integer projectId;
}
