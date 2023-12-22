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
package com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.business.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.aurine.cloudx.estate.dto.ProjectPersonDeviceDTO;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.ProjectPersonInfo;
import com.aurine.cloudx.estate.entity.SysServiceCfg;
import com.aurine.cloudx.estate.service.ProjectPersonInfoService;
import com.aurine.cloudx.estate.service.SysServiceCfgService;
import com.aurine.cloudx.estate.service.adapter.AbstractProjectPersonDeviceService;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.business.entity.DdCommunityMap;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.business.entity.DdDeviceMap;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.business.entity.DdPersonMap;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.business.mapper.DdDeviceMapMapper;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.business.mapper.DdPersonMapMapper;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.business.service.DdDeviceMapService;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoProxyVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 设备映射
 *
 * @author 王伟
 * @date 2020-11-18 17:16:08
 */
@Service
public class DdDeviceMapServiceImpl extends ServiceImpl<DdDeviceMapMapper, DdDeviceMap> implements DdDeviceMapService {

    @Resource
    private DdPersonMapMapper ddPersonMapMapper;
    @Resource
    private ProjectPersonInfoService projectPersonInfoService;
    @Resource
    private AbstractProjectPersonDeviceService abstractWebProjectPersonDeviceService;
    @Resource
    private SysServiceCfgService sysServiceCfgService;
    /**
     * 添加设备映射
     *
     * @param deviceInfo
     * @param deviceThirdId
     * @return
     */
    @Override
    public boolean addDevice(ProjectDeviceInfoProxyVo deviceInfo, DdCommunityMap communityMap, Integer deviceThirdId) {
        DdDeviceMap deviceMap = new DdDeviceMap();
        deviceMap.setCommunityid(communityMap.getCommunityid());
        deviceMap.setDeviceid(deviceThirdId);
        deviceMap.setDevicename(deviceInfo.getDeviceName());
        deviceMap.setPlatdeviceid(deviceInfo.getDeviceId());
        deviceMap.setProjectid(deviceInfo.getProjectId());
        deviceMap.setTenantId(1);
        deviceMap.setSn(deviceInfo.getSn());

        return this.save(deviceMap);
    }

    /**
     * 删除咚咚设备映射，以及所属的住户关系
     *
     * @param ddDeviceMap
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delDevice(DdDeviceMap ddDeviceMap) {
        //删除使用该设备的用户关系
        ddPersonMapMapper.delete(new QueryWrapper<DdPersonMap>().lambda().eq(DdPersonMap::getDeviceId, ddDeviceMap.getDeviceid()));

        //删除设备关联
        return this.remove(new QueryWrapper<DdDeviceMap>().lambda().eq(DdDeviceMap::getDeviceid, ddDeviceMap.getDeviceid()));
    }

    /**
     * 通过设备id，获取到映射对象
     *
     * @param deviceId
     * @return
     */
    @Override
    public DdDeviceMap getByDeviceId(String deviceId) {

        List<DdDeviceMap> deviceMapList = this.list(new QueryWrapper<DdDeviceMap>().lambda().eq(DdDeviceMap::getPlatdeviceid, deviceId));
        if (CollUtil.isNotEmpty(deviceMapList)) {
            return deviceMapList.get(0);
        }

        return null;
    }

    @Override
    public List<ProjectDeviceInfo> getDdDeviceList() {
        ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getPersonByOwner();
        if (ObjectUtil.isEmpty(projectPersonInfo)) {
            throw new RuntimeException("您在该小区下还未登记");
        }
        List<SysServiceCfg>  sysServiceCfgs = sysServiceCfgService.list(new QueryWrapper<SysServiceCfg>().lambda()
                .eq(SysServiceCfg::getServiceCode, PlatformEnum.INTERCOM_DONGDONG.code)
                .eq(SysServiceCfg::getIsActive, "1"));
        ProjectPersonDeviceDTO dto = abstractWebProjectPersonDeviceService.getDTOByPersonId(projectPersonInfo.getPersonId());

        if (ObjectUtil.isEmpty(sysServiceCfgs) || (dto.getDeviceList().size() == 0 || "0".equals(dto.getIsActive()) ||
                LocalDateTime.now().isAfter(dto.getExpTime()))) {
            return new ArrayList<>();
        }
        return baseMapper.getDdDeviceList(dto.getDeviceList().stream().map(e -> e.getDeviceId()).collect(Collectors.toList()));
    }
}
