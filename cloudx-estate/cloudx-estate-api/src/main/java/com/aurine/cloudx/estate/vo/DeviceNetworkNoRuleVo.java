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
@ApiModel(value = "设备网络配置信息-编号规则Vo")
public class DeviceNetworkNoRuleVo {
    @ApiModelProperty("梯号长度 默认 4")
    private Integer stairNoLen;
    @ApiModelProperty("房号长度 默认 4")
    private Integer roomNoLen;
    @ApiModelProperty("单元长度 默认 2")
    private Integer cellNoLen;
    @ApiModelProperty("启用单元 1=启用（默认） 2=禁用")
    private Integer useCellNo;
    @ApiModelProperty("分段参数 0 为不分段，默认 224")
    private String subSection;
}
