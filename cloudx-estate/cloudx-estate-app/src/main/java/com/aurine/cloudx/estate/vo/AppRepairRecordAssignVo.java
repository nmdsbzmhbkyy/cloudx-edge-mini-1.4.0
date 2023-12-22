package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("报修指派处理人视图")
public class AppRepairRecordAssignVo {
    /**
     * 投诉单号，uuid
     */
    @ApiModelProperty(value = "报修单号，uuid", required = true)
    private String repairId;

    /**
     * 评分1-5
     */
    @ApiModelProperty(value = "处理人id", required = true)
    private String handler;
}
