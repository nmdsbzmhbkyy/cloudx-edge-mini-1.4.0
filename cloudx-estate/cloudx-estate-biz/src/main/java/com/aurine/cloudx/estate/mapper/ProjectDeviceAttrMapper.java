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

package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.dto.DeviceAttrDto;
import com.aurine.cloudx.estate.openapi.ToOpenApi;
import com.aurine.cloudx.estate.openapi.enums.OpenApiServiceNameEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenPushSubscribeCallbackTypeEnum;
import com.aurine.cloudx.estate.vo.ProjectDeviceAttrListVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aurine.cloudx.estate.entity.ProjectDeviceAttr;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备拓展属性表
 *
 * @author xull@aurine.cn
 * @date 2020-07-03 15:18:23
 */
@Mapper
@ToOpenApi(serviceType = OpenPushSubscribeCallbackTypeEnum.OPERATE, serviceName = OpenApiServiceNameEnum.DEVICE_ATTR)
public interface ProjectDeviceAttrMapper extends BaseMapper<ProjectDeviceAttr> {

    List<ProjectDeviceAttrListVo> getDeviceAttrListVo(@Param("projectId") Integer projectId, @Param("type") String type, @Param("deviceId") String deviceId);

    List<DeviceAttrDto> getDeviceAttrKeyAndValue(@Param("sn") String sn, @Param("attrCode") String attrCode, @Param("attrValue")String attrValue);
}
