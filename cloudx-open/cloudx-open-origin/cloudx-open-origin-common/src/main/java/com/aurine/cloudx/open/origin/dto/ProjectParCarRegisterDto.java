package com.aurine.cloudx.open.origin.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Classname ProjectParCarRegisterDto
 * @Description 车辆登记
 * @Date 2022/5/15 11:46
 * @Created by admin
 */
@Data
public class ProjectParCarRegisterDto {
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    private String remark;
    /**
     * 登记id，uuid
     */
    private String registerId;
    /**
     * 车辆信息uid
     */
    private String carUid;
    /**
     * 多个车牌号
     */
    private List<String> plateNumberArray;
    /**
     * 车牌号
     */
    private String plateNumber;
    /**
     * 停车场ID
     */
    private String parkId;
    /**
     * 停车场名
     */
    private String parkName;
    /**
     * 设备名
     */
    private String deviceName;
    /**
     * 设备名
     */
    private String deviceId;
    /**
     * 车位id
     */
    private String parkPlaceId;
    /**
     * 车位地址
     */
    private String parkPlaceName;
    /**
     * 开始日期
     */
    private LocalDate startTime;

    /**
     * 结束日期
     */
    private LocalDate endTime;
    /**
     * 是否注销 0 正常 1 已注销
     */
    private String isCancelled;

    /**
     * 可通行车道ID数组 String
     */
    private String laneList;

    /**
     * 月租车收费规则id，关联project_park_billing_rule 因为一位多车调整导致收费规则需要与车辆直接绑定，而不是原本的车位
     */
    private String ruleId;

    /**
     * 操作人
     */
    private Integer operator;
    /**
     * 租户id
     */
    private Integer tenantId;
    /**
     * 项目ID
     */
    private Integer projectId;
    /**
     * 操作时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 访客车辆访问开始时间
     */
    private LocalDateTime visitBeginTime;

    /**
     * 访客车辆访问结束时间
     */
    private LocalDateTime visitEndTime;
    /**
     * 非数据库字段只是进行业务判断
     * 记录来源（默认是车辆登记-register，或者是车辆审核-audit）
     */
    private String source;
    /**
     * 关系类型 0 闲置(公共) 1 产权 2 租赁
     */
    private String relType;
    /**
     * 车主名
     */
    private String personName;
    /**
     * 车主
     */
    private String personId;
    /**
     * 费用
     */
    private BigDecimal payment;
    /**
     * 手机号
     */
    private String telephone;
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
     * 下发时间
     */
    @ApiModelProperty(value = "下发时间")
    private LocalDateTime sendTime;
    /**
     * 收费类型
     */
    private String ruleType;

    /**
     * 状态异常   0 正常    1异常
     */
    private String status;
    /**
     * 状态异常   0 正常    1异常
     */
    private String abnormalStatusCount;

    /**
     * 状态异常   0 下载成功    1 下载失败
     */
    private String dlStatus;
    /**
     * plate_number表uid
     */
    private String plateNumberUid;


    /**
     * 字典值 plate_type
     */
    @ApiModelProperty(value="字典值 plate_type")
    private String plateType;

    /**
     * 字典值 plate_color
     */
    @ApiModelProperty(value="字典值 plate_color")
    private String plateColor;

    /**
     * 字典值 vehicle_type
     */
    @ApiModelProperty(value="字典值 vehicle_type")
    private String vehicleType;

    /**
     * PlateNumberDlStatusConstant
     * 下载成功数量
     */
    private Integer success;

    /**
     * 下载失败数量
     */
    private Integer failed;

    /**
     * 下载中数量
     */
    private Integer downloading;

    /**
     *删除中数量
     */
    private Integer deleting;

    /**
     * 删除失败数量
     */
    private Integer deletingFailed;

    /**
     * 上面五个总和
     */
    private Integer allCount;


}
