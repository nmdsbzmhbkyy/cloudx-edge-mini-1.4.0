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
import com.aurine.cloudx.estate.address.AddressParseUtil;
import com.aurine.cloudx.estate.entity.ProjectInfo;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.business.entity.DdCommunityMap;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.business.entity.DdDeviceMap;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.business.entity.DdPersonMap;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.business.mapper.DdCommunityMapMapper;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.business.mapper.DdDeviceMapMapper;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.business.mapper.DdPersonMapMapper;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.business.service.DdCommunityMapService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 社区映射
 *
 * @author 王伟
 * @date 2020-11-18 17:13:14
 */
@Service
public class DdCommunityMapServiceImpl extends ServiceImpl<DdCommunityMapMapper, DdCommunityMap> implements DdCommunityMapService {


    @Resource
    private DdDeviceMapMapper ddDeviceMapMapper;
    @Resource
    private DdPersonMapMapper ddPersonMapMapper;

    /**
     * 检查项目是否已存在关联
     *
     * @param projectId
     * @return
     */
    @Override
    public boolean checkCommunityExist(int projectId) {
        return this.getByProjectId(projectId) != null;
    }

    /**
     * 通过projectId获取配置对象
     *
     * @param projectId
     * @return
     */
    @Override
    public DdCommunityMap getByProjectId(int projectId) {
        List<DdCommunityMap> resultList = this.list(new QueryWrapper<DdCommunityMap>().lambda().eq(DdCommunityMap::getProjectid, projectId));
        if (CollUtil.isNotEmpty(resultList)) {
            return resultList.get(0);
        }
        return null;
    }

    @Override
    public boolean addCommunity(ProjectInfo projectInfo, Integer villageId) {
        String addr = AddressParseUtil.addressToString(AddressParseUtil.parseAddressCodeToName(projectInfo.getProvinceCode(), projectInfo.getCityCode(), projectInfo.getCountyCode(), projectInfo.getStreetCode()));

        DdCommunityMap communityMap = new DdCommunityMap();
        communityMap.setCommunityid(villageId);
        communityMap.setCommunityname(projectInfo.getProjectName());
        communityMap.setAddr(addr);
        communityMap.setDetailaddr(projectInfo.getPropertyAddress());
        communityMap.setProjectid(projectInfo.getProjectId());
        communityMap.setTenantId(1);

        return this.save(communityMap);
    }


    /**
     * 删除社区，并清空和当前社区相关的所有数据
     *
     * @param projectId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delCommunity(int projectId) {
        DdCommunityMap communityMap = this.getByProjectId(projectId);

        //删除 人员
        ddPersonMapMapper.delete(new QueryWrapper<DdPersonMap>().lambda().eq(DdPersonMap::getProjectId, projectId));
        // 删除 设备
        ddDeviceMapMapper.delete(new QueryWrapper<DdDeviceMap>().lambda().eq(DdDeviceMap::getProjectid, projectId));
        //删除 社区
        return this.baseMapper.delete(new QueryWrapper<DdCommunityMap>().lambda().eq(DdCommunityMap::getProjectid, projectId)) >= 1;
    }
}
