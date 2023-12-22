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
import cn.hutool.core.collection.CollectionUtil;
import com.aurine.cloudx.common.core.constant.DataConstants;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.entity.ProjectDict;
import com.aurine.cloudx.estate.mapper.ProjectDictMapper;
import com.aurine.cloudx.estate.service.ProjectDictService;
import com.aurine.cloudx.estate.vo.ProjectDictVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.BeanUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目字典表
 *
 * @author cjw
 * @date 2021-07-07 08:38:37
 */
@Service
public class ProjectDictServiceImpl extends ServiceImpl<ProjectDictMapper, ProjectDict> implements ProjectDictService {

    @Resource
    private ProjectDictMapper projectDictMapper;


    @Override
    public List<ProjectDictVo> getFeeTypeList(ProjectDictVo projectDictVo) {
        return projectDictMapper.getFeeTypeList(projectDictVo);
    }

    @Override
    public List<ProjectDictVo> feeTypeActiveList() {
        ProjectDictVo projectDictVo = new ProjectDictVo();
        projectDictVo.setDictType("fee_type");
        projectDictVo.setProjectId(ProjectContextHolder.getProjectId());
        List<ProjectDictVo> feeTypeList = this.getFeeTypeList(projectDictVo);
        List<ProjectDictVo> newRecord = new ArrayList<>();
        feeTypeList.stream().forEach(item -> {
            ProjectDictVo projectDictVo1 = new ProjectDictVo();
            BeanUtil.copyProperties(item, projectDictVo1);
            projectDictVo1.setLabel(item.getDictValue());
            projectDictVo1.setValue(item.getDictCode());
            newRecord.add(projectDictVo1);

        });
        return newRecord;
    }

    @Override
    public IPage<ProjectDictVo> getFeeTypeList(Page page, ProjectDictVo projectDictVo) {
        return projectDictMapper.getFeeTypeList(page,projectDictVo);
    }

    @Override
    public R insertFeeType(ProjectDictVo projectDictVo) {
        Integer integer = projectDictMapper.selectCount(Wrappers.lambdaQuery(ProjectDict.class).eq(ProjectDict::getDictValue, projectDictVo.getDictValue())
                .eq(ProjectDict::getProjectId, ProjectContextHolder.getProjectId()));
        if(integer>0) {
            return R.failed("该费用类型已存在");
        }

        List<ProjectDict> projectDicts = projectDictMapper.selectList(Wrappers.lambdaQuery(ProjectDict.class)
                .ge(ProjectDict::getDictCode, 100)
        .eq(ProjectDict::getProjectId,ProjectContextHolder.getProjectId()));

        if (CollectionUtil.isNotEmpty(projectDicts)) {
            // 如果存在大于或者等于100 就是已经有手动添加的数据了
            String maxDictCode = projectDicts.stream().max((e1, e2) -> e1.getDictCode().compareTo(e2.getDictCode())).get().getDictCode();
            projectDictVo.setProjectId(ProjectContextHolder.getProjectId());
            Integer dictCode = Integer.valueOf(maxDictCode) +1;
            projectDictVo.setDictCode(dictCode.toString());
            projectDictMapper.insert(projectDictVo);
            return R.ok();
        }else{
            // 第一次添加费用类型
            projectDictVo.setProjectId(ProjectContextHolder.getProjectId());
            projectDictVo.setDictCode("100");
            projectDictMapper.insert(projectDictVo);
            return R.ok();
        }

    }
}
