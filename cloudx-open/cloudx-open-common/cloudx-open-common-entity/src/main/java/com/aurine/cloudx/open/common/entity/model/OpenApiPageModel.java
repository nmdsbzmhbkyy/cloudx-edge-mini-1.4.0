package com.aurine.cloudx.open.common.entity.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * OpenApi分页模型
 *
 * @author : Qiu
 * @date : 2022 01 07 16:20
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "OpenApi分页模型")
public class OpenApiPageModel<T> extends OpenApiModel<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分页信息
     */
    @JsonProperty("page")
    @JSONField(name = "page")
    @ApiModelProperty(value = "分页信息", required = true)
    @NotNull(message = "分页信息（page）不能为空")
    private Page<T> page;
}
