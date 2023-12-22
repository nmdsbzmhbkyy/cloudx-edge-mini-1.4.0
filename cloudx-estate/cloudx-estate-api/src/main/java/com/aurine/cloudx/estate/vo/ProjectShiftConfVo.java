package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalTime;

@Data
@ApiModel("排班设置上下班时间")
public class ProjectShiftConfVo {

    /**
     * 上班时间
     */
    @ApiModelProperty(value = "上班时间")
    private String workTime;
    /**
     * 下班时间
     */
    @ApiModelProperty(value = "下班时间")
    private String offworkTime;
}
