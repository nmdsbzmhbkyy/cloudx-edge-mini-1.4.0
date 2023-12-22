package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * (DeviceCountInfoVo)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/9/21 14:32
 */
@Data
@ApiModel(value = "设备统计")
public class DeviceCountInfoVo {
    @ApiModelProperty("总数设备")
    private Integer totalCount;
    @ApiModelProperty("在线设备数")
    private Integer onlineCount;
    @ApiModelProperty("离线设备数")
    private Integer outlineCount;
    @ApiModelProperty("未激活设备数")
    private Integer inactiveCount;
    @ApiModelProperty("异常设备数")
    private Integer abnormalCount;

}
