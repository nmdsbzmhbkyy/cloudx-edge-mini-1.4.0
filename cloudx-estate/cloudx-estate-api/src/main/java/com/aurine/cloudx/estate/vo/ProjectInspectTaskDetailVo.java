package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectInspectTaskDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 设备巡检任务(ProjectInspectTask)表实体类
 *
 * @author 王良俊
 * @since 2020-07-28 17:06:49
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "巡检任务vo对象")
public class ProjectInspectTaskDetailVo extends ProjectInspectTaskDetail {
    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;
    /**
     * 关联设备数
     */
    @ApiModelProperty("关联设备数")
    private String deviceNum;

    /**
     * 检查项数
     */
    @ApiModelProperty("检查项数")
    private String checkItemNum;

    /**
     * 已签到时间
     */
    @ApiModelProperty("已签到时间")
    private LocalDateTime checkInTime;

    /**
     * 已签到方式
     */
    @ApiModelProperty("已签到方式")
    private String checkInTypes;

    /**
     * 巡检情况 参考字典类型 check_in_status 1 正常 0 超时
     */
    @ApiModelProperty(value = "巡检情况 参考字典类型 check_in_status 1 正常 0 超时")
    private String checkInStatus;

}