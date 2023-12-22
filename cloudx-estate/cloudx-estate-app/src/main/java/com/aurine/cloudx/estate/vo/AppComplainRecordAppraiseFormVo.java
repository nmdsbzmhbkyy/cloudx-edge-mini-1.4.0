package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("投诉评分表单")
public class AppComplainRecordAppraiseFormVo {
    /**
     * 投诉单号，uuid
     */
    @ApiModelProperty(value = "投诉单号，uuid", required = true)
    private String complaintId;

    /**
     * 评分1-5
     */
    @ApiModelProperty(value = "评分1-5", required = true)
    private Integer score;
}
