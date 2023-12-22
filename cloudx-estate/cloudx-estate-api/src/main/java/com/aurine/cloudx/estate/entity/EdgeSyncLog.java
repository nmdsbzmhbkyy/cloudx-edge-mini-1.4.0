package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 边缘网关同步日志
 *
 * @author：zouyu
 * @data: 2022/3/22 9:43
 */
@Data
@TableName("edge_sync_log")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "边缘网关同步日志")
public class EdgeSyncLog extends Model<EdgeSyncLog> {


    /**
     * 序列
     */
    @ApiModelProperty(value = "序列")
    private Integer seq;

    /**
     * 同步id，uuid
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "同步id，uuid")
    private String syncId;

    /**
     *  1 使用边缘网关数据 2 使用云端数据
     */
    @ApiModelProperty(value = " 1 使用边缘网关数据 2 使用云端数据")
    private String syncType;

    /**
     * 同步数量
     */
    @ApiModelProperty(value = " 同步数量")
    private Integer syncNum;

    /**
     * 同步时间
     */
    @ApiModelProperty(value = "同步时间")
    private LocalDateTime syncTime;

    /**
     * 当前同步进度
     */
    @ApiModelProperty(value = "当前同步进度")
    private Double syncProcess;

    /**
     * 最终同步状态 0 失败 1 成功
     */
    @ApiModelProperty(value = "最终同步状态 0 失败 1 成功")
    private String syncStatus;

    /**
     * 错误信息
     */
    @ApiModelProperty(value = "错误信息")
    private String errMsg;

    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    private Integer projectId;


    /**
     * 租户id
     */
    @ApiModelProperty(value = "租户id")
    @TableField("tenant_id")
    private Integer tenantId;


    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    @TableField(fill = FieldFill.INSERT)
    private Integer operator;
}
