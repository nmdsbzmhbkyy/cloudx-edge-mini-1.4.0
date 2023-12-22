package com.aurine.cloudx.estate.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("访客申请拒绝视图")
public class AppVisitorRejectVo {
    /**
     * 访客申请历史记录id
     */
    @ApiModelProperty(value = "访客申请历史记录id")
    private String visitId;
    /**
     * 拒绝原因
     */
    @ApiModelProperty(value = "拒绝原因")
    private String rejectReason;
}
