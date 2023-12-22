package com.aurine.cloudx.open.origin.dto;

import com.aurine.cloudx.open.origin.entity.ProjectParkEntranceHis;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @version 1.0
 * @author： 林功鑫
 * @date： 2022-05-20 14:24
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ProjectParkEntranceHisDto extends ProjectParkEntranceHis {
    /**
     * 名字
     */
    @ApiModelProperty(value = "名字")
    private String personName;
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
     * 已停放时长
     */
    @ApiModelProperty(value = "已停放时长")
    private String stopTime;

    /**
     * 停车订单号
     */
    @TableId(type = IdType.ASSIGN_UUID)
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
     * 入场时间
     */
    @ApiModelProperty(value = "入场时间")
    private LocalDateTime enterTime;
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
     * 车场名称
     */
    @ApiModelProperty(value = "车场名称")
    private String parkName;
    /**
     * 收费类型名称
     */
    @ApiModelProperty(value = "收费类型")
    private String ruleTypeName;
    /**
     * 入场类型 1 正常入场  2 手动入场
     */
    @ApiModelProperty(value = "入场类型")
    private String enterType;
    /**
     * 出场类型 1 正常入场  2 手动入场
     */
    @ApiModelProperty(value = "出场类型")
    private String outType;
    /**
     * 车道Id
     */
    @ApiModelProperty(value = "车道Id")
    private String laneId;
}
