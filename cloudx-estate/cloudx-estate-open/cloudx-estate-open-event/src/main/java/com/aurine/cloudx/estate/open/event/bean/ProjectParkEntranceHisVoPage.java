package com.aurine.cloudx.estate.open.event.bean;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

/**
 * 车场管理
 *
 * @author 黄阳光
 * @date 2020-07-07 15:10:26
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ProjectParkEntranceHisVoPage extends Page {
    private static final long serialVersionUID = 1L;

    /**
     * 停车订单号
     */
    @ApiModelProperty(value = "停车订单号")
    private String parkOrderNo;
    /**
     * 第三方订单号
     */
    @ApiModelProperty(value = "第三方订单号")
    private String parkOrderCode;
    /**
     * 车牌号
     */
    @ApiModelProperty(value = "车牌号")
    private String plateNumber;
    /**
     * 停车场ID
     */
    @ApiModelProperty(value = "停车场ID")
    private String parkId;
    /**
     * 停车场名称
     */
    @ApiModelProperty(value = "停车场名称")
    private String parkName;
    /**
     * 规则名称
     */
    @ApiModelProperty(value = "规则名称")
    private String ruleName;
    /**
     * 规则类型
     */
    @ApiModelProperty(value = "规则类型")
    private String ruleType;
    /**
     * 订单类型 1 月租车充值 2 临时车缴费
     */
    @ApiModelProperty(value = "订单类型 1 月租车充值 2 临时车缴费")
    private String orderType;
    /**
     * 是否禁用
     */
    @ApiModelProperty(value = "是否禁用")
    private String isDisable;
    /**
     * 入场时间
     */
    @ApiModelProperty(value = "入场时间")
    private LocalDateTime enterTime;
    /**
     * 检索入场时间起始
     */
    @ApiModelProperty(value = "检索入场时间起始")
    private LocalDateTime enterTimeStart;
    /**
     * 检索入场时间结束
     */
    @ApiModelProperty(value = "检索入场时间结束")
    private LocalDateTime enterTimeEnd;
    /**
     * 入口名称
     */
    @ApiModelProperty(value = "入口名称")
    private String enterGateName;
    /**
     * 入口操作员
     */
    @ApiModelProperty(value = "入口操作员")
    private String enterOperatorName;
    /**
     * 入口车辆图片
     */
    @ApiModelProperty(value = "入口车辆图片")
    private String enterPicUrl;
    /**
     * 出厂时间
     */
    @ApiModelProperty(value = "出场时间")
    private LocalDateTime outTime;
    /**
     * 检索出场时间起始
     */
    @ApiModelProperty(value = "检索出场时间起始")
    private LocalDateTime outTimeStart;
    /**
     * 检索出场时间结束
     */
    @ApiModelProperty(value = "检索出场时间结束")
    private LocalDateTime outTimeEnd;
    /**
     * 出口名称
     */
    @ApiModelProperty(value = "出口名称")
    private String outGateName;
    /**
     * 出口操作员
     */
    @ApiModelProperty(value = "出口操作员")
    private String outOperatorName;
    /**
     * 出口车辆图片
     */
    @ApiModelProperty(value = "出口车辆图片")
    private String outPicUrl;
    /**
     * 出入状态
     */
    @ApiModelProperty(value = "出入状态 (1 入场 2 出场)")
    private String status;

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
