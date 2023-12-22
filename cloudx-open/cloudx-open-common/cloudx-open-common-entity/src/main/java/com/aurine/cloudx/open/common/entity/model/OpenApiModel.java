package com.aurine.cloudx.open.common.entity.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.base.OpenBaseModel;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * OpenApi模型
 *
 * @author : Qiu
 * @date : 2022 01 07 16:20
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "OpenApi模型")
public class OpenApiModel<T> extends OpenBaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 请求头信息
     */
    @Valid
    @JsonProperty("header")
    @JSONField(name = "header")
    @ApiModelProperty(value = "请求头信息", required = true, position = -2)
    @NotNull(message = "请求头信息（header）不能为空")
    private OpenApiHeader header;

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
