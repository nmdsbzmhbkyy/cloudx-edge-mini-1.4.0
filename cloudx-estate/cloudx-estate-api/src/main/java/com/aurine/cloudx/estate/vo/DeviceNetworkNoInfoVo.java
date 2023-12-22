package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Auther: hjj
 * @Date: 2022/6/10 15:34
 * @Description:
 */
@Data
@ApiModel(value = "设备网络配置信息-编号信息Vo")
public class DeviceNetworkNoInfoVo {
    @ApiModelProperty("设备类型")
    private Integer type;
    @ApiModelProperty("小区编号")
    private Integer areaNo;
    @ApiModelProperty("设备编号")
    private String no;
}
