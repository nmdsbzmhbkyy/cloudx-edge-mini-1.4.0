package com.aurine.cloudx.estate.excel.parking;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassName: ParCarRegisterExcel
 * @author: 王良俊 <>
 * @date:  2020年09月02日 下午03:37:59
 * @Copyright:
*/
@Data
public class ParCarRegisterExcel {

    /**
     * 车场名称 *
     * */
    @ApiModelProperty("车场名称 中文 *")
    String parkNameCh;

    /**
     * 车牌号 *
     * */
    @ApiModelProperty("车牌号")
    String plateNumber;

    /**
     * 手机号 *
     */
    @ApiModelProperty(value = "手机号 *")
    private String telephone;

    /**
     * 车主 *
     */
    @ApiModelProperty(value = "车主 中文 *")
    private String carOwner;

    /**
     * 车位类型 现在都是公共车位 *
     */
    @ApiModelProperty(value = "车位类型 中文 *")
    private String parkingSpaceTypeCh;

    /**
     * 暂时无用字段 *
     */
    @ApiModelProperty(value = "车位地址 暂时无用字段 *")
    private String parkingSpaceStr;

    /**
     * 收费方式 这里要对应ruleId收费规则ID
     */
    @ApiModelProperty(value = "收费方式 中文")
    private String ruleTypeCh;

    /**
     * 有效期 这里对应 startTime和endTime 2020-08-13至2021-09-13 如果未填写则默认到 2199-01-01
     */
    @ApiModelProperty(value = "有效期")
    private String validPeriod;

    /**
     * 缴纳金额 收费规则是免费车的时候缴纳金额无视，否则如果未填写则默认为0元
     */
    @ApiModelProperty(value = "缴纳金额")
    private BigDecimal payment;

    /**
     * 车辆信息标识
     */
    @ApiModelProperty(value="车辆信息标识")
    private String carVinInfo;

    /**
     * 车辆号牌种类 需要进行字典转换
     * */
    @ApiModelProperty(value="车辆号牌种类 中文")
    private String plateTypeCh;

    /**
     * 车辆类型 需要进行字典转换
     * */
    @ApiModelProperty(value="车辆类型 中文")
    private String vehicleTypeCh;

    /**
     * 车辆中文品牌名称
     * */
    @ApiModelProperty(value="车辆中文品牌名称")
    private String brandName;

    /**
     * 车辆型号
     */
    @ApiModelProperty(value="车辆型号")
    private String vehicleModel;

    /**
     * 车辆长度(毫米)
     */
    @ApiModelProperty(value="车辆长度(毫米)")
    private Integer length;

    /**
     * 车辆宽度(毫米)
     */
    @ApiModelProperty(value="车辆宽度(毫米)")
    private Integer width;

    /**
     * 车辆高度(毫米)
     */
    @ApiModelProperty(value="车辆高度(毫米)")
    private Integer height;

    /**
     * 车辆颜色 中文 需要进行字典转换
     */
    @ApiModelProperty(value="车辆颜色 中文")
    private String vehicleColorCh;

    /**
     * 车辆简要情况
     */
    @ApiModelProperty(value="车辆简要情况")
    private String remark;
}
