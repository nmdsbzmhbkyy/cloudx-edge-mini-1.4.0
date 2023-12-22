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

import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.estate.entity.ProjectPersonAttr;
import com.aurine.cloudx.estate.mapper.ProjectPersonAttrMapper;
import com.aurine.cloudx.estate.service.ProjectPersonAttrService;
import com.aurine.cloudx.estate.vo.ProjectPersonAttrFormVo;
import com.aurine.cloudx.estate.vo.ProjectPersonAttrListVo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 人员属性拓展表
 *
 * @author xull@aurine.cn
 * @date 2020-07-03 16:24:30
 */
@Service
public class ProjectPersonAttrServiceImpl extends ServiceImpl<ProjectPersonAttrMapper, ProjectPersonAttr> implements ProjectPersonAttrService {

    @Override
    public boolean updatePersonAttrList(ProjectPersonAttrFormVo projectPersonAttrFormVo) {
        remove(Wrappers.lambdaUpdate(ProjectPersonAttr.class)
                .eq(ProjectPersonAttr::getPersonId, projectPersonAttrFormVo.getPersonId()));
        List<ProjectPersonAttr> projectPersonAttrs = new ArrayList<>();
        // 添加非空判断 当人员属性不为空时 新增拓展属性
        if (projectPersonAttrFormVo.getProjectPersonAttrList() != null && projectPersonAttrFormVo.getProjectPersonAttrList().size() > 0) {
            projectPersonAttrFormVo.getProjectPersonAttrList().forEach(e -> {
                ProjectPersonAttr projectPersonAttr = new ProjectPersonAttr();
                BeanUtils.copyProperties(e, projectPersonAttr);
                projectPersonAttr.setPersonId(projectPersonAttrFormVo.getPersonId());
                projectPersonAttr.setPersonType(projectPersonAttrFormVo.getType());
                projectPersonAttr.setProjectId(projectPersonAttrFormVo.getProjectId());
                if (projectPersonAttr.getAttrValue() != null) {
                    projectPersonAttrs.add(projectPersonAttr);
                }
            });
            if (projectPersonAttrs.size() > 0) {
                return saveBatch(projectPersonAttrs);
            }
        }
        return true;
    }

    @Override
    public boolean updatePersonAttrList(List<ProjectPersonAttrFormVo> personAttrFormVoList) {
        List<String> personIdList = personAttrFormVoList.stream().map(ProjectPersonAttrFormVo::getPersonId).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(personIdList)) {
            boolean remove = remove(Wrappers.lambdaUpdate(ProjectPersonAttr.class).in(ProjectPersonAttr::getPersonId, personIdList));
        }
        List<ProjectPersonAttr> projectPersonAttrs = new ArrayList<>();
        for (ProjectPersonAttrFormVo projectPersonAttrFormVo : personAttrFormVoList) {
            // 添加非空判断 当人员属性不为空时 新增拓展属性
            if (projectPersonAttrFormVo.getProjectPersonAttrList() != null && projectPersonAttrFormVo.getProjectPersonAttrList().size() > 0) {
                projectPersonAttrFormVo.getProjectPersonAttrList().forEach(e -> {
                    ProjectPersonAttr projectPersonAttr = new ProjectPersonAttr();
                    BeanUtils.copyProperties(e, projectPersonAttr);
                    projectPersonAttr.setPersonId(projectPersonAttrFormVo.getPersonId());
                    projectPersonAttr.setPersonType(projectPersonAttrFormVo.getType());
                    projectPersonAttr.setProjectId(projectPersonAttrFormVo.getProjectId());
                    if (projectPersonAttr.getAttrValue() != null) {
                        projectPersonAttrs.add(projectPersonAttr);
                    }
                });
            }
        }
        if (CollUtil.isNotEmpty(projectPersonAttrs)) {
            return saveBatch(projectPersonAttrs);
        }
        return false;
    }

    @Override
    public boolean removePersonAttrList(String personId) {
        remove(Wrappers.lambdaUpdate(ProjectPersonAttr.class)
                .eq(ProjectPersonAttr::getPersonId, personId));
        return true;
    }

    @Override
    public List<ProjectPersonAttrListVo> getPersonAttrListVo(Integer projectId, String type, String personId) {
        return baseMapper.getPersonAtrrListVo(projectId, type, personId);
    }
}
