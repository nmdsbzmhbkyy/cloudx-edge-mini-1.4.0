package com.aurine.cloudx.open.origin.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户注销信息DTO
 */
@Data
@ApiModel(value = "用户注销信息DTO")
public class SysUserLockDTO {
    @ApiModelProperty(value="电话", required = true)
    private String phone;

    @ApiModelProperty(value="注销时间(格式 yyyy-MM-dd HH:mm:ss)")
    private String updateTime;
}
