

package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 设备聚集报警规则表
 *
 * @author 黄健杰
 * @date 2022-01-27 09:13:10
 */
@Data
@TableName("project_device_gather_alarm_rule")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "设备聚集报警规则表")
public class ProjectDeviceGatherAlarmRule extends Model<ProjectDeviceGatherAlarmRule> {
    private static final long serialVersionUID = 1L;

    /**
     * 规则id, 32位uuid
     */
    @ApiModelProperty(value = "规则id, 32位uuid")
    @TableId(type = IdType.ASSIGN_UUID)
    private String ruleId;

    /**
     * 设备id, 32位uuid
     */
    @ApiModelProperty(value = "设备id, 32位uuid")
    private String deviceId;

    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    private Integer projectId;

    /**
     * 是否启用
     */
    @ApiModelProperty(value = "是否启用 1 启用 0 禁用")
    private String isAlarm;

    /**
     * 报警人数
     */
    @ApiModelProperty(value = "报警人数")
    private Integer personNum;

    /**
     * 持续时间（分）
     */
    @ApiModelProperty(value = "持续时间（分）")
    private Integer duration;

    /**
     * 上报间隔（预留字段）
     */
    @ApiModelProperty(value = "上报间隔（预留字段）")
    private Integer interval;

    /**
     * 报警星期，逗号分隔
     */
    @ApiModelProperty(value = "报警星期，逗号分隔")
    private String weeks;

    /**
     * 报警时段，逗号分隔
     */
    @ApiModelProperty(value = "报警时段，逗号分隔")
    private String hours;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
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
