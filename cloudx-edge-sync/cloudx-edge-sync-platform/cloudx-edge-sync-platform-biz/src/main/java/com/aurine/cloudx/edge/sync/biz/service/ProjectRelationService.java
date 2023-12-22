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

package com.aurine.cloudx.edge.sync.biz.service;

import com.aurine.cloudx.edge.sync.common.entity.po.ProjectRelation;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author cjw
 * @date 2021-12-23 15:31:43
 */
public interface ProjectRelationService extends IService<ProjectRelation> {
    /**
     * 根据项目projectCode获取对象
     *
     * @param ProjectCode
     * @return ProjectRelation
     */
    ProjectRelation getByProjectCode(String ProjectCode);

    /**
     * 根据项目projectCode获取对象
     *
     * @param ProjectUUID
     * @return ProjectRelation
     */
    ProjectRelation getByProjectUUID(String ProjectUUID);

    /**
     * 根据项目UUID获取对象
     *
     * @param ProjectUUID
     * @return ProjectRelation
     */
    ProjectRelation getByProjectRelation(String ProjectUUID, String projectCode);

    /**
     * 获取正在级联或入云的项目，不同端实现不一致
     * 保存到关系表中
     *
     * @return uuid列表
     */
    List<ProjectRelation> getProjectInfoList();

    /**
     * 新增 projectRelation关系表关系
     * 新增 mqtt 订阅
     * 新增 OpenApi 订阅回调地址
     *
     * @param projectRelation
     */
    Boolean addProjectRelation(ProjectRelation projectRelation);

    /**
     * 删除 projectRelation关系表关系
     * 删除 mqtt 订阅
     * 删除 OpenApi 订阅回调地址
     *
     * @param projectUUID
     */
    Boolean removeProjectRelation(String projectUUID, String projectCode);

    /**
     * 获取数据库projectUuid列表
     *
     * @return
     */
    List<String> getUuidList();

    /**
     * 获取数据库projectCode列表
     *
     * @return
     */
    List<String> getCodeList();
}
