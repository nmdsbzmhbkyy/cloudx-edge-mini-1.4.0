package com.aurine.cloudx.estate.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: 顾文豪
 * @Date: 2023/11/13 15:42
 * @Remarks:
 **/
@Data
@ApiModel(value = "人员信息修改手机号dto")
public class OpenApiProjectPersonInfoUpdatePhoneDto {
    /**
     * 修改前手机号
     */
    @ApiModelProperty(value = "修改前手机号")
    private String oldTelephone;

    /**
     * 修改后手机号
     */
    @ApiModelProperty(value = "修改后手机号")
    private String newTelephone;
}
