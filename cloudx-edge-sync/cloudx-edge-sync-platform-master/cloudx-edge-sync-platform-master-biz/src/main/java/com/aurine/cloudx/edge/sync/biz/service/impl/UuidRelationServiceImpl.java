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
package com.aurine.cloudx.edge.sync.biz.service.impl;

import com.aurine.cloudx.edge.sync.biz.mapper.UuidRelationMapper;
import com.aurine.cloudx.edge.sync.biz.service.UuidRelationService;
import com.aurine.cloudx.edge.sync.common.entity.po.UuidRelation;
import com.aurine.cloudx.edge.sync.common.entity.dto.TaskInfoDto;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author pigx code generator
 * @date 2022-01-05 16:09:50
 */
@Service
public class UuidRelationServiceImpl extends ServiceImpl<UuidRelationMapper, UuidRelation> implements UuidRelationService {

    @Resource
    private UuidRelationMapper uuidRelationMapper;

    private UuidRelation getUuidByMarks(UuidRelation uuidRelation) {
        LambdaQueryWrapper<UuidRelation> queryWrapper = new LambdaQueryWrapper<UuidRelation>()
                .eq(UuidRelation::getTenantId, uuidRelation.getTenantId())
                .eq(UuidRelation::getProjectUUID, uuidRelation.getProjectUUID())
                .eq(UuidRelation::getServiceName, uuidRelation.getServiceName())
                .orderByDesc(UuidRelation::getSeq)
                .last("limit 1");
        if (uuidRelation.getUuid() != null) {
            queryWrapper.eq(UuidRelation::getUuid, uuidRelation.getUuid());
        } else if (uuidRelation.getThirdUuid() != null) {
            queryWrapper.eq(UuidRelation::getThirdUuid, uuidRelation.getThirdUuid());
        } else {
            return null;
        }
        return baseMapper.selectOne(queryWrapper);
    }

    private UuidRelation buildPublicUUidParams(TaskInfoDto taskInfoDto) {
        UuidRelation uuidRelation = new UuidRelation();
        uuidRelation.setProjectUUID(taskInfoDto.getProjectUUID());
        uuidRelation.setServiceName(taskInfoDto.getServiceName());
        uuidRelation.setTenantId(taskInfoDto.getTenantId());
        return uuidRelation;
    }

    @Override
    public UuidRelation getByThirdUuid(TaskInfoDto taskInfoDto) {
        UuidRelation uuidRelation = buildPublicUUidParams(taskInfoDto);
        uuidRelation.setThirdUuid(taskInfoDto.getUuid());
        return getUuidByMarks(uuidRelation);
    }

    @Override
    public UuidRelation getByUuid(TaskInfoDto taskInfoDto) {
        UuidRelation uuidRelation = buildPublicUUidParams(taskInfoDto);
        uuidRelation.setUuid(taskInfoDto.getUuid());
        return getUuidByMarks(uuidRelation);
    }

    /**
     * 类型为OperateType时
     * 保存uuid关系表数据,
     * 只在业务操作的新增操作处理请求
     *
     * @param taskInfoDto
     */
    @Override
    public Boolean saveUuidRelation(TaskInfoDto taskInfoDto) {
        UuidRelation uuidRelation = buildPublicUUidParams(taskInfoDto);
        uuidRelation.setUuid(taskInfoDto.getUuid());
        uuidRelation.setThirdUuid(taskInfoDto.getThirdUuid());
        uuidRelation.setOldMd5(taskInfoDto.getNewMd5());
        return this.save(uuidRelation);
    }


    /**
     * 类型为OperateType时
     * 保存uuid关系表数据,
     * 只在业务操作的新增操作处理请求
     *
     * @param taskInfoDto
     */
    @Override
    public Boolean updateOldMd5ByUuid(TaskInfoDto taskInfoDto) {
        UuidRelation uuidRelation = getByUuid(taskInfoDto);
        uuidRelation.setOldMd5(taskInfoDto.getNewMd5());
        return uuidRelationMapper.updateByMarks(uuidRelation) > 0;
    }

    /**
     * 类型为OperateType时
     * 保存uuid关系表数据,
     * 只在业务操作的新增操作处理请求
     *
     * @param taskInfoDto
     */
    @Override
    public Boolean updateOldMd5ByThirdUuid(TaskInfoDto taskInfoDto) {
        UuidRelation uuidRelation = getByThirdUuid(taskInfoDto);
        uuidRelation.setOldMd5(taskInfoDto.getNewMd5());
        return uuidRelationMapper.updateByMarks(uuidRelation) > 0;
    }

    @Override
    public Boolean deleteByUuid(TaskInfoDto taskInfoDto) {
        UuidRelation uuidRelation = buildPublicUUidParams(taskInfoDto);
        uuidRelation.setUuid(taskInfoDto.getUuid());
        return uuidRelationMapper.deleteByMarks(uuidRelation) > 0;
    }

    @Override
    public Boolean deleteByThirdUuid(TaskInfoDto taskInfoDto) {
        UuidRelation uuidRelation = buildPublicUUidParams(taskInfoDto);
        uuidRelation.setThirdUuid(taskInfoDto.getUuid());
        return uuidRelationMapper.deleteByMarks(uuidRelation) > 0;
    }

    @Override
    public Boolean checkOldMd5(String oldMd5) {
        return this.count(new LambdaQueryWrapper<UuidRelation>()
                .eq(UuidRelation::getOldMd5, oldMd5)) > 0;
    }
}
