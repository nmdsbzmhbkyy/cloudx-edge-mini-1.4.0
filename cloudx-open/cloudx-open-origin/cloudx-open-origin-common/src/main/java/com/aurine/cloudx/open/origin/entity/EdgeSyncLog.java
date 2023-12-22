package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
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
public class EdgeSyncLog extends OpenBasePo<EdgeSyncLog> {
    private static final long serialVersionUID = 1L;

    /**
     * 同步ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty("同步ID")
    private String syncId;

    /**
     * 1 使用边缘网关数据 2 使用云端数据
     */
    @ApiModelProperty("1 使用边缘网关数据 2 使用云端数据")
    private String syncType;


    /**
     * 同步数量
     */
    @ApiModelProperty(value = " 同步数量")
    private Integer syncNum;

    /**
     * 同步时间
     */
    @ApiModelProperty("同步时间")
    private LocalDateTime syncTime;

    /**
     * 当前同步进度
     */
    @ApiModelProperty("当前同步进度")
    private BigDecimal syncProcess;

    /**
     *最终同步状态 0失败 1成功
     */
    @ApiModelProperty("最终同步状态 0失败 1成功")
    private Character syncStatus;

    /**
     * 错误信息
     */
    @ApiModelProperty("错误信息")
    private String errMsg;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     *更新时间
     */
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    /**
     * 项目ID
     */
    @ApiModelProperty("项目ID")
    private Integer projectId;

    /**
     * tenantId
     */
    @ApiModelProperty("tenantId")
    @TableField("tenant_id")
    private Integer tenantId;

    /**
     * 发起人
     */
    @ApiModelProperty("发起人")
    private Integer operator;



}
