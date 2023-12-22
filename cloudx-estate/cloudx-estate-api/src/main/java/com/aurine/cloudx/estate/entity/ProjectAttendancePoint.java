package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
/**
 * 考勤点设置(ProjectAttendancePoint)表实体类
 *
 * @author xull
 * @since 2021-03-03 10:52:24
 */
@Data
@TableName("project_attendance_point")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "考勤点设置(ProjectAttendancePoint)")
public class ProjectAttendancePoint extends Model<ProjectAttendancePoint> {

    private static final long serialVersionUID = 107713526202970557L;


    /**
     * 考勤点id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "考勤点id")
    private String pointId;


    /**
     * 考勤点地址
     */
    @ApiModelProperty(value = "考勤点地址")
    private String pointAddress;


    /**
     * 经度
     */
    @ApiModelProperty(value = "经度")
    private Double lon;


    /**
     * 纬度
     */
    @ApiModelProperty(value = "纬度")
    private Double lat;


    /**
     * 高度
     */
    @ApiModelProperty(value = "高度")
    private Double alt;


    /**
     * 坐标
     */
    @ApiModelProperty(value = "坐标")
    private String gisArea;


    /**
     * 坐标系代码
     */
    @ApiModelProperty(value = "坐标系代码")
    private String gisType;


    /**
     * 有效范围（米）
     */
    @ApiModelProperty(value = "有效范围（米）")
    private Integer pointPrecision;


    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private Integer operator;


    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;


}
