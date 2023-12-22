package com.aurine.cloudx.open.origin.dto;

import com.aurine.cloudx.open.origin.entity.ProjectCarPreRegister;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @version 1.0
 * @author： 林功鑫
 * @date： 2022-05-24 17:25
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ProjectCarPreRegisterDto extends ProjectCarPreRegister {
    /**
     * 开始日期
     */
    @ApiModelProperty(value = "开始日期")
    private LocalDate startTime;

    /**
     * 结束日期
     */
    @ApiModelProperty(value = "结束日期")
    private LocalDate endTime;

    /**
     * 车位id
     */
    private String parkPlaceId;
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
    @ApiModelProperty(value = "计费规则ID")
    private String ruleId;

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

    /**
     * 下发时间起始
     */
    @ApiModelProperty(value = "检索入场时间起始")
    private LocalDateTime enterTimeStart;
    /**
     * 下发时间结束
     */
    @ApiModelProperty(value = "检索入场时间结束")
    private LocalDateTime enterTimeEnd;
    /**
     * 车牌下发状态
     */
    private String plateNumberStatus;
    /**
     * 车牌
     */
    private String plateNumber;
    /**
     * 下发时间
     */
    @ApiModelProperty(value = "下发时间")
    private LocalDateTime sendTime;
    /**
     * 停车场ID
     */
    private String parkId;

    /**
     * 收费类型
     */
    private String ruleType;

    /**
     * 状态异常   0 正常    1异常
     */
    private String status;

    /**
     * 状态异常   0 下载成功    1 下载失败
     */
    private String dlStatus;

    /**
     * 已选择车道id集合
     */
    private List<String> checkedLanes;
    /**
     * parCarRegisterId
     */
    private String registerId;

    /**
     * 车辆预登记ID（用于审核通过的时候修改审核状态）
     */
    private String preRegId;

    /**
     * 停车区域ID
     * */
    private String parkRegionId;

    /**
     * 车牌号是否已被登记
     * */
    private String plateNumberHasRegister;

    /**
     * 日期数组（后端返回的对象没有这个属性会导致前端组件无法选择时间）
     * */
    private String[] dateArray;


    /**
     * 审核状态
     * */
    private String auditStatus;

}
