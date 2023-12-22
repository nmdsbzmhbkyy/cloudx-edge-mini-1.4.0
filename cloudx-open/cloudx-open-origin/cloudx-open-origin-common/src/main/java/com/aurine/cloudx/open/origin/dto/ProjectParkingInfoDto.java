

package com.aurine.cloudx.open.origin.dto;

import com.aurine.cloudx.open.origin.entity.ProjectParkingInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 停车场
 *
 * @author 王伟
 * @date 2020-05-07 09:13:25
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ProjectParkingInfoDto extends ProjectParkingInfo {
    private static final long serialVersionUID = 1L;


    /**
     * 停车场ID
     */
    private String parkId;
    /**
     * 停车场名称
     */
    private String parkName;

    /**
     * 停车场编码，可用于第三方编码
     */
    private String parkCode;
    /**
     * 停车位总数
     */
    private Integer parkNum;
    /**
     * 车辆数
     */
    private Integer carNum;

    /**
     * 已经被使用的车位数
     */
    private Integer usedPark;
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
     * 使用状态
     */
    private String usageStatus;

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
     * 车场车位满位处理方式
     */
    private Integer fullLot;

    /**
     * 车辆重复入场处理方式 0禁止入场 1允许入场
     */
    private Integer repeatEnter;
    /**
     * 项目id
     */
    private Integer projectId;

    /**
     *
     */
    private Integer tenantId;
    /**
     * 对接厂商名字
     */
    private String manufacturer;
    /**
     * 是否有公共车位
     */
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


    /**
     * 对接id
     */
    private String thirdpartyCode;

}
