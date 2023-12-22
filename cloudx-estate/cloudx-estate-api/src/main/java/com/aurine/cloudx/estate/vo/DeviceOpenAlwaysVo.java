package com.aurine.cloudx.estate.vo;

import cn.hutool.core.codec.Base64;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 门常开设置VO
 */
@Data
public class DeviceOpenAlwaysVo {

    /**
     * 设备ID
     * */
    @ApiModelProperty("设备ID")
    private String deviceId;

    /**
     * 门行为
     * */
    @ApiModelProperty("门行为，1=启用，0=关闭")
    private Integer doorAction;
}
