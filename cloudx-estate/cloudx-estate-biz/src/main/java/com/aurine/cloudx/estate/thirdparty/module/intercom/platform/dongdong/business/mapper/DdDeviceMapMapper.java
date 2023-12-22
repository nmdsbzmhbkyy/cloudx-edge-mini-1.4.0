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

package com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.business.mapper;

import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.business.entity.DdDeviceMap;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备映射
 *
 * @author 王伟
 * @date 2020-11-18 17:16:08
 */
@Mapper
public interface DdDeviceMapMapper extends BaseMapper<DdDeviceMap> {

    List<ProjectDeviceInfo> getDdDeviceList(@Param("list") List<String> deviceList);
}
