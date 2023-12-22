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

package com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.business.service;

import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.business.entity.DdCommunityMap;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoProxyVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.business.entity.DdDeviceMap;

import java.util.List;

/**
 * 设备映射
 *
 * @author 王伟
 * @date 2020-11-18 17:16:08
 */
public interface DdDeviceMapService extends IService<DdDeviceMap> {

    /**
     * 添加设备映射
     *
     * @param deviceInfo
     * @param communityMap
     * @param deviceThirdId
     * @return
     */
    boolean addDevice(ProjectDeviceInfoProxyVo deviceInfo, DdCommunityMap communityMap, Integer deviceThirdId);

    /**
     * 删除咚咚设备映射，以及所属的住户关系
     * @param ddDeviceMap
     * @return
     */
    Boolean delDevice(DdDeviceMap ddDeviceMap);

    /**
     * 通过设备id，获取到映射对象
     *
     * @param deviceId
     * @return
     */
    DdDeviceMap getByDeviceId(String deviceId);

    /**
     * 通过设备id，获取到映射对象
     *
     * @return
     */
    List<ProjectDeviceInfo> getDdDeviceList();

}
