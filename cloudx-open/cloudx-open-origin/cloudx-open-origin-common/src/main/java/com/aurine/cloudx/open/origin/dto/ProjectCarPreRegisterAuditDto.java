package com.aurine.cloudx.open.origin.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 车辆预登记审核通过vo对象
 */
@Data
public class ProjectCarPreRegisterAuditDto {

    /**
     * 车辆预登记ID（用于审核通过的时候修改审核状态）
     */
    private String preRegId;
    private String PlateNumber;
    private String parkId;
    /**
     * 车位id
     */
    private String ParkPlaceId;
    /**
     * 停车区域ID
     */
    private String parkRegionId;

    /**
     * 车牌号是否已被登记
     */
    private String plateNumberHasRegister;

    /**
     * 日期数组（后端返回的对象没有这个属性会导致前端组件无法选择时间）
     */
    private String[] dateArray;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String telephone;

    /**
     * 车主名
     */
    @ApiModelProperty(value = "车主名")
    private String personName;

    /**
     * 关系类型 0 闲置(公共) 1 产权 2 租赁
     */
    @ApiModelProperty(value = "关系类型 0 闲置(公共) 1 产权 2 租赁")
    private String relType;


    // 计费规则已改为登记对象属性
    /**
     * 计费规则ID
     */
/*    @ApiModelProperty(value = "计费规则ID")
    private String ruleId;*/

    /**
     * 费用
     */
    @ApiModelProperty(value = "费用")
    private BigDecimal payment;


    /**
     * 车辆id，uuid
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "车辆id，uuid")
    private String carUid;

    /**
     * 车主
     */
    @ApiModelProperty(value = "车主")
    private String personId;

    /**
     * 车辆信息标识
     */
    @ApiModelProperty(value = "车辆信息标识")
    private String carVinInfo;

    /**
     * 字典值 plate_type
     */
    @ApiModelProperty(value = "字典值 plate_type")
    private String plateType;

    /**
     * 字典值 plate_color
     */
    @ApiModelProperty(value = "字典值 plate_color")
    private String plateColor;

    /**
     * 字典值 vehicle_type
     */
    @ApiModelProperty(value = "字典值 vehicle_type")
    private String vehicleType;

    /**
     * 车辆中文品牌名称
     */
    @ApiModelProperty(value = "车辆中文品牌名称")
    private String brandName;

    /**
     * 车辆型号
     */
    @ApiModelProperty(value = "车辆型号")
    private String vehicleModel;

    /**
     * 车辆长度(毫米)
     */
    @ApiModelProperty(value = "车辆长度(毫米)")
    private Integer length;

    /**
     * 车辆宽度(毫米)
     */
    @ApiModelProperty(value = "车辆宽度(毫米)")
    private Integer width;

    /**
     * 车辆高度(毫米)
     */
    @ApiModelProperty(value = "车辆高度(毫米)")
    private Integer height;

    /**
     * 字典值 vehicle_color
     */
    @ApiModelProperty(value = "A.白 B.灰 C.黄 D.粉 E.红 F.紫 G.绿 H.蓝 I.棕 J.黑 Z.其他 字典值 vehicle_color")
    private String vehicleColor;

    /**
     * 车辆简要情况
     */
    @ApiModelProperty(value = "车辆简要情况")
    private String remark;

    /**
     * 月租费用（元）
     */
    @ApiModelProperty(value = "月租费用（元）")
    private BigDecimal monthlyRent;

    /**
     * 这里作为Excel导入时用
     */
    @ApiModelProperty(value = "车场名")
    private String parkName;


    /**
     * 非数据库字段只是进行业务判断
     */
    @ApiModelProperty(value = "记录来源（默认是车辆登记-register，或者是车辆审核-audit）")
    private String source;

}
