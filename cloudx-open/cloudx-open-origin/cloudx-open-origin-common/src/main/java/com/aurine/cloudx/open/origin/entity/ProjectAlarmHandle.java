

package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 报警事件处理
 *
 * @author 黄阳光
 * @date 2020-06-04 08:31:21
 */
@Data
@TableName("project_alarm_handle")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "报警事件处理")
public class ProjectAlarmHandle extends OpenBasePo<ProjectAlarmHandle> {
    private static final long serialVersionUID = 1L;

    /**
     * 序列
     */
    @TableId
    @ApiModelProperty(value = "序列，自增")
    private Integer seq;

    /**
     * 事件id，uuid
     */
    @ApiModelProperty(value = "事件id，uuid")
    private String eventId;

    /**
     * 时限
     */
    @ApiModelProperty(value = "时限")
    private String timeLeave;

    /**
     * 开始处理时间
     */
    @ApiModelProperty(value = "开始处理时间")
    private LocalDateTime handleBeginTime;

    /**
     * 结束处理时间
     */
    @ApiModelProperty(value = "结束处理时间")
    private LocalDateTime handleEndTime;

    /**
     * 处理时长
     */
    @ApiModelProperty(value = "处理时长")
    private String dealDuration;

    /**
     * 处理结果
     */
    @ApiModelProperty(value = "处理结果")
    private String result;

    /**
     * 图片
     */
    @ApiModelProperty(value = "图片")
    private String picUrl;
}
