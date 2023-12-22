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

package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 车场计费规则查询条件
 *
 * @author 王伟
 * @date 2020-07-07 13:34:12
 */
@Data
@ApiModel(value = "车场计费规则查询条件")
public class ProjectParkBillingRuleSearchConditionVo {

    /**
     * 车场ID
     */
    @ApiModelProperty(value = "车场ID")
    private String parkId;

    /**
     * 是否禁用 0 启用 1 禁用
     */
    @ApiModelProperty(value = "是否禁用 0 启用 1 禁用")
    private String isDisable;


}
