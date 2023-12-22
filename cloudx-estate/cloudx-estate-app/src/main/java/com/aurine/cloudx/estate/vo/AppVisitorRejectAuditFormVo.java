package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("访客拒绝表单")
public class AppVisitorRejectAuditFormVo {
    @ApiModelProperty(value = "访客申请历史记录id", required = true)
    private String visitId;

    @ApiModelProperty(value = "拒绝原因")
    private String rejectReason;
}