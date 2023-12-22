package com.aurine.cloudx.estate.vo;


import com.alibaba.fastjson.JSON;
import com.aurine.cloudx.estate.entity.EdgeCascadeConf;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>项目入云配置（云端）</p>
 * @author : 王良俊
 * @date : 2021-12-10 15:31:17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EdgeCascadeConfVo extends EdgeCascadeConf {

    /**
     * 入云状态
     */
    @ApiModelProperty("入云状态")
    private Character cloudStatus;

    /**
     * 级联状态
     */
    @ApiModelProperty("级联状态")
    private Character cascadeStatus;

    /**
     * 连接码
     */
    @ApiModelProperty("连接码")
    private String connectionCode;

    /**
     * 同步方式
     */
    @ApiModelProperty("同步方式")
    private Character syncType;

    /**
     * 主边缘网关IP
     */
    @ApiModelProperty("主边缘网关IP")
    private String parentEdgeIp;

    /**
     * 级联码（上级）
     */
    @ApiModelProperty("级联码（上级）")
    private String parentConnectCode;

    /**
     * 入云申请申请ID
     */
    @ApiModelProperty("入云申请申请ID")
    private String cloudRequestId;

    /**
     * 级联从边缘网关数量（审核中和已级联的）
     */
    @ApiModelProperty("级联从边缘网关数量（审核中和已级联的）")
    private Integer slaveNum;

    /**
     * 级联申请申请ID
     */
    @ApiModelProperty("级联申请申请ID")
    private String cascadeRequestId;

    /**
     * 同步进度
     */
    private BigDecimal cloudSyncProcess;

    /**
     * 当前级联项目级联同步进度
     */
    private BigDecimal slaveSyncProcess;

    /**
     * 当前项目级联主边缘网关同步进度
     */
    private BigDecimal masterSyncProcess;

    /**
     * 能否启用联网
     */
    private boolean canEnableNetwork;

    /**
     * 无法启用联网原因
     */
    private String cannotEnableNetworkReason;

    /**
     * 能否禁用联网
     */
    private boolean canDisableNetwork;

    /**
     * 无法禁用联网原因
     */
    private String cannotDisableNetworkReason;

    /**
     * 是否启用级联
     */
    private boolean canEnableCascade;

    /**
     * 是否禁用级联
     */
    private boolean canDisableCascade;


    /**
     * 无法禁用级联原因
     */
    private String cannotDisableCascadeReason;

    /**
     * 是否是主边缘网关
     */
    private boolean master;

    /**
     * 当前边缘网关自有项目的ID（不是级联项目）
     */
    private Integer originProjectId;
    /**
     * syncType选择2使用云端数据时启用，0未删除，1已删除，2删除中
     */
    private String delStatus;

    /**
     * 0 未同步 1 已同步
     */
    private Character isSync;


    /**
     * 是否允许使用云端项目数据为主
     */
    private boolean result;

}