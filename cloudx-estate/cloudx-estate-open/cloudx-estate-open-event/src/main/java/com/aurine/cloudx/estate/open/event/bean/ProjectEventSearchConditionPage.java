

package com.aurine.cloudx.estate.open.event.bean;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 通行事件记录
 *
 * @author pigx code generator
 * @date 2020-05-20 13:27:59
 */
@Data
@ApiModel(value = "通行事件记录查询参数")
public class ProjectEventSearchConditionPage extends Page {
    /**
     * 项目id
     */
//    @ApiModelProperty(value="项目id")
//    private Integer projectId;
    /**
     * 事件类型
     */
    @ApiModelProperty(value="事件类型")
    private String eventType;
    /**
     * 人员姓名
     */
    @ApiModelProperty(value = "人员姓名")
    private String personName;
    /**
     * 设备名称
     */
    @ApiModelProperty(value = "设备名称")
    private String deviceName;
    /**
     * 设备区域名称
     */
    @ApiModelProperty(value = "设备区域名称")
    private String regionName;
    /**
     *  查询时间开始
     */
    @ApiModelProperty(value = "开始时间")
    private LocalDateTime effTime;
    /**
     *  查询时间结束
     */
    @ApiModelProperty(value = "结束时间")
    private LocalDateTime expTime;

    /**
     * 项目id
     */
    @ApiModelProperty("项目id")
    private Integer projectId;

    /**
     *
     */
    @ApiModelProperty(value = "", hidden = true)
    @TableField(value = "tenant_id")
    private Integer tenantId;
    }
