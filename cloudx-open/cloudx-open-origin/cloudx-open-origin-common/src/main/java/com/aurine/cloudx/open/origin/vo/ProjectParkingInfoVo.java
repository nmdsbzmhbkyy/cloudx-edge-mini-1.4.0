
package com.aurine.cloudx.open.origin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 停车区域
 *
 * @author 王伟
 * @date 2020-05-07 09:13:25
 */
@Data
@ApiModel(value = "停车场")
public class ProjectParkingInfoVo {


    /**
     * 停车场ID
     */
    @NotEmpty
    @ApiModelProperty(value = "停车场ID")
    private String parkId;
    /**
     * 停车场名称
     */
    @ApiModelProperty(value = "停车场名称、车位区域")
    private String parkName;

    /**
     * 停车场编码，可用于第三方编码
     */
    @ApiModelProperty(value = "停车场编码，可用于第三方编码")
    private String parkCode;
    /**
     * 停车位总数
     */
    @ApiModelProperty(value = "停车位总数")
    private Integer parkNum;
    /**
     * 车辆数
     */
    @ApiModelProperty(value = "车辆数")
    private Integer carNum;

    /**
     * 已经被使用的车位数
     */
    @ApiModelProperty(value = "已使用车位数")
    private Integer usedPark;
    /**
     * 对接状态 1 已连接 0 未连接
     */
    @ApiModelProperty(value = "对接状态 1 已连接 0 未连接")
    private char status;

    /**
     * 楼层
     */
    @ApiModelProperty(value = "楼层")
    private String floor;

    /**
     * 临时车收费规则id，关联project_park_billing_rule
     */
    @ApiModelProperty(value = "临时车收费规则id，关联project_park_billing_rule")
    private String ruleId;

    /**
     * 对接第三方厂商
     */
    @ApiModelProperty(value = "对接第三方厂商")
    private String company;

    /**
     * 使用状态
     */
    @ApiModelProperty(value = "使用状态")
    private String usageStatus;

    /**
     * 是否开启一位多车 0否 1是
     */
    @ApiModelProperty(value = "是否开启一位多车")
    private Integer isMultiCar;

    /**
     * 一位多车规则 0停满不可入场 1停满按临时车收费
     */
    @ApiModelProperty(value = "一位多车规则")
    private Integer multiCarRule;

    /**
     * 月租车过期处理方式 0过期禁止入场 1过期1天后禁止入场
     */
    @ApiModelProperty(value = "月租车过期处理方式")
    private Integer rentCarExpHanMethod;

    /**
     * 车场车位满位处理方式 0满位禁止入场 1满位允许入场
     */
    @ApiModelProperty(value = "车场车位满位处理方式")
    private Integer fullLot;

    /**
     * 车辆重复入场处理方式 0禁止入场 1允许入场
     */
    @ApiModelProperty(value = "车辆重复入场处理方式")
    private Integer repeatEnter;
    /**
     * 项目id
     */
    @ApiModelProperty("项目id")
    private Integer projectId;

    /**
     *
     */
    @ApiModelProperty(value = "", hidden = true)
    private Integer tenantId;
    /**
     * 对接厂商名字
     */
    @ApiModelProperty(value = "对接第三方厂商")
    private String manufacturer;
    /**
     * 是否有公共车位
     */
//    @ApiModelProperty(value = "是否有公共车位")
//    private boolean hasPublic;

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
     * 单位名称
     */
    private String contactPhone;

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

}
