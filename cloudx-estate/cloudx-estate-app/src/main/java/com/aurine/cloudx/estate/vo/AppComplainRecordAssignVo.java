package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("投诉指派处理人视图")
public class AppComplainRecordAssignVo {
    /**
     * 投诉单号，uuid
     */
    @ApiModelProperty(value = "投诉单号，uuid", required = true)
    private String complaintId;

    /**
     * 评分1-5
     */
    @ApiModelProperty(value = "处理人id", required = true)
    private String handler;
}
