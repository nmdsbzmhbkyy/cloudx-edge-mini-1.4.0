package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectAttendance;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author cjw
 * @description:
 * @date 2021/3/5 10:26
 */
@Data
@ApiModel(value = "考勤记录查询条件")
public class ProjectAttendanceQueryVo extends ProjectAttendance {

    /**
     * 筛选时间范围
     */
    @ApiModelProperty(value = "筛选时间范围")
    private String[] dateRange;

    /**
     * 筛选某个月数据
     */
    @ApiModelProperty(value = "筛选某个月数据")
    private String yearMonth;

    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    private Integer projectId;

    /**
     * 租户id
     */
    @TableField("tenant_Id")
    private Integer tenantId;

    @ApiModelProperty(value = "排班年份")
    private String Year;

    /**
     * 排班月份
     */
    @ApiModelProperty(value = "排班月份")
    private String Month;

    @ApiModelProperty(value = "排班年份2")
    private String Year2;

    /**
     * 排班月份
     */
    @ApiModelProperty(value = "排班月份2")
    private String Month2;

    /**
     * 排班月份
     */
    @ApiModelProperty(value = "排班月份2")
    private String detailId;


    @ApiModelProperty(value = "选择导出条数")
    List<ProjectAttendanceQueryVo> selectRow;

}
