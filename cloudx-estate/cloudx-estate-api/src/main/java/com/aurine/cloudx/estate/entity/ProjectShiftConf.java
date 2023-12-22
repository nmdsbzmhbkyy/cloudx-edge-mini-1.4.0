package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 班次配置
 *
 * @author guhl@aurine.cn
 * @date 2020-07-23 08:36:54
 */
@Data
@TableName("project_shift_conf")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "班次配置")
public class ProjectShiftConf extends Model<ProjectShiftConf> {
    private static final long serialVersionUID = 1L;

    /**
     * 班次id，uuid
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "班次id，uuid")
    private String shiftId;
    /**
     * 班次名称
     */
    @ApiModelProperty(value = "班次名称")
    private String shiftName;
    /**
     * 班次 1 一天1班 2 一天2班 3 一天3班
     */
    @ApiModelProperty(value = "班次 1 一天1班 2 一天2班 3 一天3班")
    private String shiftType;
    /**
     * 1班名称
     */
    @ApiModelProperty(value = "1班名称")
    private String shiftName1;
    /**
     * 1班上班时间
     */
    @ApiModelProperty(value = "1班上班时间")
    @TableField(fill = FieldFill.UPDATE)
    private String shiftTimeBegin1;
    /**
     * 1班下班时间
     */
    @ApiModelProperty(value = "1班下班时间")
    @TableField(fill = FieldFill.UPDATE)
    private String shiftTimeEnd1;
    /**
     * 1班名称
     */
    @ApiModelProperty(value = "2班名称")
    private String shiftName2;
    /**
     * 1班上班时间
     */
    @ApiModelProperty(value = "2班上班时间")
    @TableField(fill = FieldFill.UPDATE)
    private String shiftTimeBegin2;
    /**
     * 1班下班时间
     */
    @ApiModelProperty(value = "2班下班时间")
    @TableField(fill = FieldFill.UPDATE)
    private String shiftTimeEnd2;
    /**
     * 1班名称
     */
    @ApiModelProperty(value = "3班名称")
    private String shiftName3;
    /**
     * 1班上班时间
     */
    @ApiModelProperty(value = "3班上班时间")
    @TableField(fill = FieldFill.UPDATE)
    private String shiftTimeBegin3;
    /**
     * 1班下班时间
     */
    @ApiModelProperty(value = "3班下班时间")
    @TableField(fill = FieldFill.UPDATE)
    private String shiftTimeEnd3;
    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    private Integer projectId;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
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
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
