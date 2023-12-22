package com.aurine.cloudx.estate.vo;

import com.pig4cloud.pigx.common.core.sensitive.Sensitive;
import com.pig4cloud.pigx.common.core.sensitive.SensitiveTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 用户信息
 */
@Data
@ApiModel(value = "用户信息")
public class AppUserInfoVo {


    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    private Integer userId;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String phone;

    /**
     * 头像
     */
    @ApiModelProperty(value = "头像照片 Base64")
    private String avatar;

    /**
     * 性别
     */
    @ApiModelProperty(value = "性别 0 女 1 男 ")
    private String sex;

    /**
     * 真实姓名
     */
    @ApiModelProperty(value = "真实姓名")
    private String trueName;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名", required = true)
    private String username;

    @ApiModelProperty("适老化状态")
    private String isAgingUi;
}
