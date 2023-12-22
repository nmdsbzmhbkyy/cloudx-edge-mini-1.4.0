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


import com.aurine.cloudx.estate.entity.ProjectDeviceGatherAlarmRule;
import com.aurine.cloudx.estate.mapper.ProjectDeviceGatherAlarmRuleMapper;
import com.aurine.cloudx.estate.service.ProjectDeviceGatherAlarmRuleService;
import com.aurine.cloudx.estate.vo.ProjectDeviceGatherAlarmRuleVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 设备聚集报警规则表
 *
 * @author 黄健杰
 * @date 2022-02-07
 */
@Service
public class ProjectDeviceGatherAlarmRuleServiceImpl extends ServiceImpl<ProjectDeviceGatherAlarmRuleMapper, ProjectDeviceGatherAlarmRule> implements ProjectDeviceGatherAlarmRuleService {


    @Override
    public ProjectDeviceGatherAlarmRuleVo getProjectDeviceGatherAlarmRuleVoByDeviceId(String deviceId) {
        return baseMapper.getProjectDeviceGatherAlarmRuleVoByDeviceId(deviceId);
    }

    @Override
    public void removeByDeviceId(String deviceId) {
        this.remove(new LambdaQueryWrapper<ProjectDeviceGatherAlarmRule>().eq(ProjectDeviceGatherAlarmRule::getDeviceId, deviceId));
    }

    @Override
    public Integer updateByDeviceId(ProjectDeviceGatherAlarmRule rule) {
        return baseMapper.updateByDeviceId(rule);
    }
}
