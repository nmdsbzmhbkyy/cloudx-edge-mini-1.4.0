package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 设备巡检任务明细(ProjectInspectTaskDetail)表实体类
 *
 * @author 王良俊
 * @since 2020-07-28 17:06:58
 */
@Data
@TableName("project_inspect_task_detail")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "设备巡检任务明细(ProjectInspectTaskDetail)")
public class ProjectInspectTaskDetail extends OpenBasePo<ProjectInspectTaskDetail> {

    private static final long serialVersionUID = 752386055516002775L;


    /**
     * 明细id，uuid
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "明细id，uuid")
    private String detailId;

    /**
     * 巡检任务id
     */
    @ApiModelProperty(value = "巡检任务id")
    private String taskId;

    /**
     * 巡检点id
     */
    @ApiModelProperty(value = "巡检点id")
    private String pointId;

    /**
     * 巡检点名称
     */
    @ApiModelProperty(value = "巡检点名称")
    private String pointName;

    /**
     * 位置
     */
    @ApiModelProperty(value = "位置")
    private String regionId;

    /**
     * 巡检情况  参考字典类型 inspect_result  0 未巡检 1 正常 2 异常
     */
    @ApiModelProperty(value = "巡检情况  参考字典类型 inspect_result  0 未巡检 1 正常 2 异常")
    private String result;

    /**
     * 执行人员列表，逗号分隔
     */
    @ApiModelProperty(value = "执行人员列表，逗号分隔")
    private String execStaffName;

    /**
     * 完成时间
     */
    @ApiModelProperty(value = "完成时间")
    private LocalDateTime endTime;

    /**
     * 温度（℃）
     */
    @ApiModelProperty(value = "是否测量温度")
    private String isTemper;

    /**
     * 湿度（%RH）
     */
    @ApiModelProperty(value = "是否测量湿度")
    private String isHumidity;

    /**
     * 温度（℃）
     */
    @ApiModelProperty(value = "温度（℃）")
    private Double temperature;

    /**
     * 湿度（%RH）
     */
    @ApiModelProperty(value = "湿度（%RH）")
    private Double humidity;

}