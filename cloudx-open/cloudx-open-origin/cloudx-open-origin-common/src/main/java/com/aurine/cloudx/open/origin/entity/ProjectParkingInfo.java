

package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 停车场
 *
 * @author 王伟
 * @date 2020-05-07 09:13:25
 */
@Data
@TableName(value = "project_parking_info", schema = "aurine_parking")
@EqualsAndHashCode(callSuper = true)
public class ProjectParkingInfo extends OpenBasePo<ProjectParkingInfo> {
    private static final long serialVersionUID = 1L;

    /**
     * 停车场ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String parkId;
    /**
     * 停车场编码，可用于第三方编码
     */
    private String parkCode;
    /**
     * 停车场名称
     */
    private String parkName;
    /**
     * 停车位总数
     */
    private Integer parkNum;
    /**
     * 备注说明
     */
    private String note;
    /**
     * 备注说明
     */
    private String usageStatus;
    /**
     * 停车场类型
     */
    private String parkType;
    /**
     * 管理单位
     */
    private String orgUnit;
    /**
     * 单位名称
     */
    private String unitName;
    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 是否开启一位多车 0否 1是
     */
    private Integer isMultiCar;

    /**
     * 一位多车规则 0停满不可入场 1停满按临时车收费
     */
    private Integer multiCarRule;

    /**
     * 月租车过期处理方式 0过期禁止入场 1过期1天后禁止入场
     */
    private Integer rentCarExpHanMethod;

    /**
     * 车场车位满位处理方式 0 满位禁止入场 1满位允许入场
     */
    private Integer fullLot;

    /**
     * 车辆重复入场处理方式 0禁止入场 1允许入场
     */
    private Integer repeatEnter;
    /**
     * 经度
     */
    private Double lon;
    /**
     * 纬度
     */
    private Double lat;
    /**
     * 高度
     */
    private Double alt;
    /**
     * 坐标
     */
    private String gisArea;
    /**
     * 坐标系代码
     */
    private String gisType;

    /**
     * 操作人
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer operator;
    /**
     * 操作时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 对接状态 1 已连接 0 未连接
     */
    private char status;

    /**
     * 楼层
     */
    private String floor;

    /**
     * 临时车收费规则id，关联project_park_billing_rule
     */
    private String ruleId;

    /**
     * 对接第三方厂商
     */
    private String company;
    /**
     * 项目id
     */
    private Integer projectId;

    /**
     *
     */
    @TableField(value = "tenant_id")
    private Integer tenantId;
}
