package com.aurine.cloudx.open.origin.vo;

import lombok.Data;

import java.util.List;

/**
 * 批量添加考勤点表单
 */
@Data
public class ProjectAttendancePointForm {
    /**
     * 范围
     */
    private Integer pointPrecision;
    /**
     * 考勤地址列表
     */
    private List<ProjectAttendancePointPlaceVo> placeVo;
}
