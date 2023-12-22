package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("报修评价表单")
public class AppRepairRecordAppraiseFormVo {
    @ApiModelProperty(value = "报修ID", required = true)
    private String repairId;

    @ApiModelProperty(value = "评分1-5", required = true)
    private Integer score;
}
