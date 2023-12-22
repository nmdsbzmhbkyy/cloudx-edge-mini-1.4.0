package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("打卡定位信息表单")
public class AppAttendanceLocalFormVo {

    /**
     * 考勤记录id
     */
    @ApiModelProperty(value = "考勤记录id", required = true)
    private String attendanceId;

    /**
     * 经度
     */
    @ApiModelProperty(value = "经度", required = true)
    private Double lon;
    /**
     * 纬度
     */
    @ApiModelProperty(value = "纬度", required = true)
    private Double lat;
}
