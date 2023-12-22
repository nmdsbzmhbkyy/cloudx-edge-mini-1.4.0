package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectAttendance;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * @author cjw
 * @description: 考勤汇总查询
 * @date 2021/3/11 10:19
 */
@Data
public class ProjectAttdanceSummaryVo extends ProjectAttendance {
    /**
     * 应勤天数
     */
    @ApiModelProperty(value = "应勤天数")
    private Integer workDay;
    /**
     * 迟到天数
     */
    @ApiModelProperty(value = "迟到天数")
    private Integer lateDay;
    /**
     * 早退天数
     */
    @ApiModelProperty(value = "早退天数")
    private Integer leaveEarlyDay;
    /**
     * 正常上班天数
     */
    @ApiModelProperty(value = "正常上班天数")
    private Integer normalWorkDay;
    /**
     * 旷工天数
     */
    @ApiModelProperty(value = "旷工天数")
    private Integer outWork;
    /**
     * 是否包括离职员工
     */
    @ApiModelProperty(value = "是否包括离职员工")
    private Boolean leaveStaff;

    private String yearMonth;

    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    private Integer projectId;

    /**
     * 租户id
     */
    @ApiModelProperty(value = "租户id")
    private Integer tenantId;

    @ApiModelProperty(value = "时间范围")
    private LocalDate [] dateRange;

    @ApiModelProperty(value = "选择导出条数")
    private List<ProjectAttdanceSummaryVo> selectRow;
}
