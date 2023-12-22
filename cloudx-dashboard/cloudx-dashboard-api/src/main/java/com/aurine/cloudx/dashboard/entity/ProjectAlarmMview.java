package com.aurine.cloudx.dashboard.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 报警事件统计
 *
 * @ClassName: ProjectComplaintRecordMview
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-29 14:37
 * @Copyright:
 */
@Data
@TableName("PROJECT_ALARM_MVIEW")
public class ProjectAlarmMview extends BaseDashboardEntity {


    /**
     * 未处理
     */
    @ApiModelProperty("未处理")
    @TableField("CNT_NOT_PROCESS")
    private Long notProcess;
    /**
     * 处理中
     */
    @ApiModelProperty("处理中")
    @TableField("CNT_IN_PROCESS")
    private Long inProcess;
    /**
     * 已处理
     */
    @ApiModelProperty("已处理")
    @TableField("CNT_PROCESSED")
    private Long processed;
}
