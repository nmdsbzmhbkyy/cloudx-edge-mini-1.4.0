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
package com.aurine.cloudx.estate.service.impl;

import com.aurine.cloudx.estate.entity.SysDeviceTypeThirdPartyConfig;
import com.aurine.cloudx.estate.mapper.SysDevicetypeThirdpartyConfigMapper;
import com.aurine.cloudx.estate.service.SysDeviceTypeThirdPartyConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

/**
 * 第三方接口设备配置
 *
 * @author 王伟
 * @date 2020-08-13 14:23:08
 */
@Service
@RefreshScope
public class SysDeviceTypeThirdPartyConfigServiceImpl extends ServiceImpl<SysDevicetypeThirdpartyConfigMapper, SysDeviceTypeThirdPartyConfig> implements SysDeviceTypeThirdPartyConfigService {


}
