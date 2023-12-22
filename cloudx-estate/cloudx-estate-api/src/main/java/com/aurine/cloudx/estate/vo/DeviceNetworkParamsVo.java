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
@ApiModel(value = "设备网络配置信息-网络参数Vo")
public class DeviceNetworkParamsVo {
    @ApiModelProperty("设备IP")
    private String ip;
    @ApiModelProperty("子网掩码")
    private String mask;
    @ApiModelProperty("网关")
    private String gateway;
    @ApiModelProperty("主DNS")
    private String dns1;
    @ApiModelProperty("备DNS")
    private String dns2;
    @ApiModelProperty("中心服务器IP")
    private String centerIp;
}
