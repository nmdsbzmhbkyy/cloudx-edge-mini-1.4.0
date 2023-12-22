package com.aurine.cloudx.estate.vo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("涂鸦账号密码")
public class AppTuyaUserVo {
    /**
     * 用户名
     */
    @NotBlank
    @JSONField(name = "username")
    @ApiModelProperty("用户名")
    private String username;
    /**
     * 密码
     */
    @NotBlank
    @JSONField(name = "password")
    @ApiModelProperty("密码，默认为123456")
    private String password;
    /**
     * 应用标识
     */
    @NotBlank
    @ApiModelProperty("应用标识")
    private String SCHEMA;
}
