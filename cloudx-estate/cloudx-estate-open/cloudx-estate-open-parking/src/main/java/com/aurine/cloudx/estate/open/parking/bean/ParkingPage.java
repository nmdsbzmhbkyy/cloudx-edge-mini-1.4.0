package com.aurine.cloudx.estate.open.parking.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("停车场条件对象")
@Data
public class ParkingPage extends Page {
    @ApiModelProperty("停车场ID")
    private String parkId;
    @ApiModelProperty("停车场名称、车位区域")
    private String parkName;
    @ApiModelProperty("停车场编码，可用于第三方编码")
    private String parkCode;
    @ApiModelProperty("停车位总数")
    private Integer parkNum;
    @ApiModelProperty("车辆数")
    private Integer carNum;
    @ApiModelProperty("已使用车位数")
    private Integer usedPark;
    @ApiModelProperty("对接状态 1 已连接 0 未连接")
    private char status;
    @ApiModelProperty("楼层")
    private String floor;
    @ApiModelProperty("临时车收费规则id，关联project_park_billing_rule")
    private String ruleId;
    @ApiModelProperty("对接第三方厂商")
    private String company;
    @ApiModelProperty("项目id")
    private Integer projectId;
}
