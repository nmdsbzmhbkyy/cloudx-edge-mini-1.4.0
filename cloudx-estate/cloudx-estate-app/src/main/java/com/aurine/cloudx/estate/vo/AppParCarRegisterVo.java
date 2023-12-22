package com.aurine.cloudx.estate.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@ApiModel(value = "车辆登记VO")
public class AppParCarRegisterVo {

    @ApiModelProperty(value = "车场Id(获取停车场列表)", required = true)
    private String parkId;

    /**
     * 车牌号
     */
    @ApiModelProperty(value = "车牌号", required = true)
    private String plateNumber;
    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号", required = true)
    private String telephone;

    /**
     * 车主名
     */
    @ApiModelProperty(value = "车主名", required = true)
    private String personName;

    /**
     * 关系类型 0 闲置(公共) 1 产权 2 租赁
     */
    @ApiModelProperty(value = "关系类型 0 闲置(公共) 1 产权 2 租赁", required = true)
    private String relType;

    /**
     * 计费规则ID
     */
    @ApiModelProperty(value = "计费规则ID(获取停车场收费列表)", required = true)
    private String ruleId;

    /**
     * 费用
     */
    @ApiModelProperty(value = "费用", required = true)
    private BigDecimal payment;


    /**
     * 车辆id，uuid
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value="车辆id，uuid")
    private String carUid;


    /**
     * 车位id
     */
     @ApiModelProperty(value = "车位id（获取车位列表）")
    private String parkPlaceId;
    @ApiModelProperty(value = "车位区域Id（获取车位区域列表）", required = true)
    private String parkRegionId;

    /**
     * 开始日期
     */
    @ApiModelProperty(value = "开始日期（格式 yyyy-mm-dd）", required = true)
    private LocalDate startTime;
    /**
     * 结束日期
     */
    @ApiModelProperty(value = "结束日期（格式 yyyy-mm-dd）", required = true)
    private LocalDate endTime;




}
