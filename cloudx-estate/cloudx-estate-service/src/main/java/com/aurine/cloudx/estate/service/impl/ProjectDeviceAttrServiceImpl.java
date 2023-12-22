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

import com.aurine.cloudx.estate.vo.ProjectDeviceAttrFormVo;
import com.aurine.cloudx.estate.vo.ProjectDeviceAttrListVo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aurine.cloudx.estate.entity.ProjectDeviceAttr;
import com.aurine.cloudx.estate.mapper.ProjectDeviceAttrMapper;
import com.aurine.cloudx.estate.service.ProjectDeviceAttrService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 设备拓展属性表
 *
 * @author xull@aurine.cn
 * @date 2020-07-03 15:18:23
 */
@Service
public class ProjectDeviceAttrServiceImpl extends ServiceImpl<ProjectDeviceAttrMapper, ProjectDeviceAttr> implements ProjectDeviceAttrService {

    @Override
    public List<ProjectDeviceAttrListVo> getDeviceAttrListVo(Integer projectId, String type, String deviceId) {

        return baseMapper.getDeviceAttrListVo(projectId, type, deviceId);
    }

    @Override
    public boolean updateDeviceAttrList(ProjectDeviceAttrFormVo projectDeviceAttrFormVo) {
        remove(Wrappers.lambdaUpdate(ProjectDeviceAttr.class)
                .eq(ProjectDeviceAttr::getDeviceId, projectDeviceAttrFormVo.getDeviceId()));
        List<ProjectDeviceAttr> projectDeviceAttrs = new ArrayList<>();
        //添加非空判断 非空时新增设备属性 xull@aurine.cn 2020年7月9日 15点04分
        if (projectDeviceAttrFormVo.getProjectDeviceAttrList() != null && projectDeviceAttrFormVo.getProjectDeviceAttrList().size() > 0) {
            projectDeviceAttrFormVo.getProjectDeviceAttrList().forEach(e -> {
                ProjectDeviceAttr projectDeviceAttr = new ProjectDeviceAttr();
                BeanUtils.copyProperties(e, projectDeviceAttr);
                projectDeviceAttr.setProjectId(projectDeviceAttrFormVo.getProjectId());
                projectDeviceAttr.setDeviceId(projectDeviceAttrFormVo.getDeviceId());
                projectDeviceAttr.setDeviceType(projectDeviceAttrFormVo.getType());
                if (projectDeviceAttr.getAttrValue()!=null){
                    projectDeviceAttrs.add(projectDeviceAttr);
                }
            });
            return saveBatch(projectDeviceAttrs);
        }
        return true;
    }
}
