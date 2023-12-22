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

package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.SysThirdPartyInterfaceConfig;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 第三方接口配置
 *
 * @author 王伟
 * @date 2020-08-27 14:20:04
 */
@Data
@ApiModel(value = "第三方接口配置Vo")
public class SysThirdPartyInterfaceConfigVo extends SysThirdPartyInterfaceConfig {

    @ApiModelProperty(value = "设备类型")
    private String deviceType;

    @ApiModelProperty(value = "是否要更新配置 1 是，0 否")
    private String updateState;

}
