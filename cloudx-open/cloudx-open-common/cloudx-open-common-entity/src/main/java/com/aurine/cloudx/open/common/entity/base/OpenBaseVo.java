package com.aurine.cloudx.open.common.entity.base;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Open基础Vo
 * 用于Vo对象统一时使用，减少Vo对象重复定义，Vo需要继承该类
 *
 * @author : Qiu
 * @date : 2021 12 31 14:24
 */

@Data
@ApiModel(value = "Open基础Vo")
public class OpenBaseVo implements Serializable {

    /**
     * 租户id
     */
    @JsonProperty("tenantId")
    @JSONField(name = "tenantId")
    @ApiModelProperty(value = "租户id", hidden = true, position = 7)
    @Max(value = Integer.MAX_VALUE, message = "租户id（tenantId）数值过大")
    private Integer tenantId;

    /**
     * 操作人
     */
    @JsonProperty("operator")
    @JSONField(name = "operator")
    @ApiModelProperty(value = "操作人", hidden = true, position = 8)
    @Max(value = Integer.MAX_VALUE, message = "操作人（operator）数值过大")
    private Integer operator;

    /**
     * 操作时间，东八区
     */
    @JsonProperty("createTime")
    @JSONField(name = "createTime", format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "操作时间，东八区", hidden = true, position = 9)
    private LocalDateTime createTime;

    /**
     * 更新时间，东八区
     */
    @JsonProperty("updateTime")
    @JSONField(name = "updateTime", format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间，东八区", hidden = true, position = 9)
    private LocalDateTime updateTime;
}
