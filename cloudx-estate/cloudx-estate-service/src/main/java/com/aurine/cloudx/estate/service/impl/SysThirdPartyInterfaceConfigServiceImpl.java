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

import cn.hutool.core.collection.CollectionUtil;
import com.aurine.cloudx.common.core.constant.enums.DeviceTypeEnum;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.SysThirdPartyConstant;
import com.aurine.cloudx.estate.entity.SysDeviceTypeThirdPartyConfig;
import com.aurine.cloudx.estate.entity.SysThirdPartyInterfaceConfig;
import com.aurine.cloudx.estate.mapper.SysThirdpartyInterfaceConfigMapper;
import com.aurine.cloudx.estate.service.SysDeviceTypeThirdPartyConfigService;
import com.aurine.cloudx.estate.service.SysThirdPartyInterfaceConfigService;
import com.aurine.cloudx.estate.vo.SysThirdPartyInterfaceConfigVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

/**
 * 第三方接口配置
 *
 * @author 王伟
 * @date 2020-08-27 14:20:04
 */
@Service
public class SysThirdPartyInterfaceConfigServiceImpl extends ServiceImpl<SysThirdpartyInterfaceConfigMapper, SysThirdPartyInterfaceConfig> implements SysThirdPartyInterfaceConfigService {

}
