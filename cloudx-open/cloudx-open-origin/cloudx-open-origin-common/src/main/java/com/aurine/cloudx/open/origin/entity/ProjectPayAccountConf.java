/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 项目收款账号管理
 *
 * @author
 * @date 2021-07-06 08:42:02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "项目收款账号管理")
public class ProjectPayAccountConf extends OpenBasePo<ProjectPayAccountConf> {
    private static final long serialVersionUID = 1L;


    /**
     * 账号id，uuid
     */
    @ApiModelProperty(value = "账号id，uuid")
    @TableId(type = IdType.ASSIGN_UUID)

    private String accountId;
    /**
     * 商户名称
     */
    @ApiModelProperty(value = "商户名称")
    private String mechantName;
    /**
     * 收款通道 '1' 支付宝  '2' 微信
     */
    @ApiModelProperty(value = "收款通道 '1' 支付宝  '2' 微信")
    private String payType;
    /**
     * 微信公众号支付商户号
     */
    @ApiModelProperty(value = "微信公众号支付商户号")
    private String wechatOfficialAccountMerchantNo;
    /**
     * 微信公众号账号appid
     */
    @ApiModelProperty(value = "微信公众号账号appid")
    private String wechatOfficialAccountAppId;
    /**
     * 微信公众号支付秘钥
     */
    @ApiModelProperty(value = "微信公众号支付秘钥")
    private String wechatOfficialAccountPaySecret;
    /**
     * 微信app支付商户号
     */
    @ApiModelProperty(value = "微信app支付商户号")
    private String wechatAppMerchantNo;
    /**
     * 微信开放账号appid
     */
    @ApiModelProperty(value = "微信开放账号appid")
    private String wechatOpenAppid;
    /**
     * 微信app支付秘钥
     */
    @ApiModelProperty(value = "微信app支付秘钥")
    private String wechatAppPaySecret;
    /**
     * 支付宝收款用户id
     */
    @ApiModelProperty(value = "支付宝收款用户id")
    private String alipayAccountId;
    /**
     * 支付宝商户秘钥
     */
    @ApiModelProperty(value = "支付宝商户秘钥")
    private String alipayMechatSecret;
    /**
     * 账号状态 '0' 禁用 '1' 启用
     */
    @ApiModelProperty(value = "账号状态 '0' 禁用 '1' 启用")
    private String accountStatus;
    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    private Integer projectId;
}
