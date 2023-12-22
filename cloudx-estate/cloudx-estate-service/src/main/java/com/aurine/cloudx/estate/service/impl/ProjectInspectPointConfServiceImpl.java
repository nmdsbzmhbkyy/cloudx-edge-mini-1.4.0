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

import cn.hutool.core.bean.BeanUtil;
import com.aurine.cloudx.estate.constant.InspectPointIsBluetoothConstants;
import com.aurine.cloudx.estate.constant.InspectStatusConstants;
import com.aurine.cloudx.estate.entity.ProjectInspectPointDeviceRel;
import com.aurine.cloudx.estate.entity.ProjectInspectRoutePointConf;
import com.aurine.cloudx.estate.service.ProjectInspectPointDeviceRelService;
import com.aurine.cloudx.estate.service.ProjectInspectRoutePointConfService;
import com.aurine.cloudx.estate.vo.ProjectInspectPointConfSearchConditionVo;
import com.aurine.cloudx.estate.vo.ProjectInspectPointConfVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aurine.cloudx.estate.entity.ProjectInspectPointConf;
import com.aurine.cloudx.estate.mapper.ProjectInspectPointConfMapper;
import com.aurine.cloudx.estate.service.ProjectInspectPointConfService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 设备巡检点配置
 *
 * @author 王良俊
 * @date 2020-07-23 16:26:33
 */
@Service
public class ProjectInspectPointConfServiceImpl extends ServiceImpl<ProjectInspectPointConfMapper, ProjectInspectPointConf>
        implements ProjectInspectPointConfService {

    @Resource
    ProjectInspectPointConfMapper projectInspectPointConfMapper;
    @Resource
    ProjectInspectRoutePointConfService projectInspectRoutePointConfService;
    @Resource
    ProjectInspectPointDeviceRelService projectInspectPointDeviceRelService;

    @Override
    public Page<ProjectInspectPointConfVo> fetchList(Page<ProjectInspectPointConfVo> page, ProjectInspectPointConfSearchConditionVo query) {
        return projectInspectPointConfMapper.fetchList(page, query);
    }

    @Override
    public List<ProjectInspectPointConfVo> listByInspectPointByRouteId(String inspectRouteId) {
        return projectInspectPointConfMapper.listInspectionByRouteId(inspectRouteId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveInspectPoint(ProjectInspectPointConfVo projectInspectPointConfVo) {
        int count = this.count(new QueryWrapper<ProjectInspectPointConf>().lambda().eq(ProjectInspectPointConf::getPointName, projectInspectPointConfVo.getPointName()));
        if (count != 0) {
            throw new RuntimeException("巡检点名重复");
        }
        ProjectInspectPointConf projectInspectPointConf = new ProjectInspectPointConf();
        BeanUtil.copyProperties(projectInspectPointConfVo, projectInspectPointConf);
        projectInspectPointConf.setStatus(InspectStatusConstants.ACTIVITY);
        // 所在区域默认是1
        projectInspectPointConf.setRegionId("1");
        // 先保存巡检点以获取到该巡检点的id
        boolean save = this.save(projectInspectPointConf);
        // 保存巡检点和设备之间的关系
        projectInspectPointDeviceRelService.saveOrUpdatePointDeviceRel(projectInspectPointConf.getPointId(),
                projectInspectPointConfVo.getDeviceIdArr());
        return save;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeInspectPoint(String pointId) {
        // 删除巡检点和路线的关系
        projectInspectRoutePointConfService.remove(new QueryWrapper<ProjectInspectRoutePointConf>().lambda()
                .eq(ProjectInspectRoutePointConf::getInspectPointId, pointId));
        // 删除巡检点和设备的关系
        projectInspectPointDeviceRelService.remove(new QueryWrapper<ProjectInspectPointDeviceRel>().lambda()
                .eq(ProjectInspectPointDeviceRel::getPointId, pointId));
        return this.removeById(pointId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateInspectPoint(ProjectInspectPointConfVo pointConfVo) {
        projectInspectPointDeviceRelService.saveOrUpdatePointDeviceRel(pointConfVo.getPointId(),
                pointConfVo.getDeviceIdArr());
        if (InspectPointIsBluetoothConstants.NO.equals(pointConfVo.getIsBlueTeeth())){
            pointConfVo.setDeviceId("");
        }
        ProjectInspectPointConf projectInspectPointConf = new ProjectInspectPointConf();
        BeanUtil.copyProperties(pointConfVo, projectInspectPointConf);
        return this.updateById(projectInspectPointConf);
    }

}
