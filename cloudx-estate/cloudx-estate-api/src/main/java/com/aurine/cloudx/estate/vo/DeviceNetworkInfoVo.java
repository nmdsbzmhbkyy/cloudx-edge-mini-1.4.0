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
@ApiModel(value = "设备网络配置信息Vo")
public class DeviceNetworkInfoVo {
    @ApiModelProperty("网络参数")
    private DeviceNetworkParamsVo netParams;
    @ApiModelProperty("编号信息")
    private DeviceNetworkNoInfoVo noInfo;
    @ApiModelProperty("编号规则")
    private DeviceNetworkNoRuleVo noRules;
    @ApiModelProperty("设备语言 2052=中文（默认） 1028=繁体 1033=英文 1037=希伯来语")
    private Integer language;
    @ApiModelProperty("分段描述 例如：[\"栋\",\"单元\",\"房\"]")
    private String[] sectionDesc;
    @ApiModelProperty("校验值")
    private String checkValue;
}
