package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("访客申请通过表单")
public class AppPassAuditByPersonVo {

    @ApiModelProperty(value = "访客申请历史记录id", required = true)
    private String visitId;
}
