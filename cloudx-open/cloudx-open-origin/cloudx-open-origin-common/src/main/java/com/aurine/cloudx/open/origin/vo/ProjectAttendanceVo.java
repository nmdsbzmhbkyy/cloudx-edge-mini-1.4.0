package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectAttendance;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 员工考勤打卡(ProjectAttendance)表实体类
 *
 * @author xull
 * @since 2021-03-03 10:52:24
 */
@Data
@ApiModel(value = "员工考勤打卡视图")
public class ProjectAttendanceVo extends ProjectAttendance {

    /**
     * 员工是否进入打卡范围
     */
    @ApiModelProperty("员工是否进入打卡范围 0 未进入打卡范围 1 进入打卡范围")
    private String isClockIn = "0";

    /**
     * 进入打卡范围的地点
     */
    @ApiModelProperty("进入打卡范围的地点")
    private String area;

}
