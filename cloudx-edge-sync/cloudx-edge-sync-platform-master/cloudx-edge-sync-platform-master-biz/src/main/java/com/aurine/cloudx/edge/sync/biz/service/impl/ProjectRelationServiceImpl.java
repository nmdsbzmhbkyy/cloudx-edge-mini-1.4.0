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

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.edge.sync.biz.mqtt.MqttClient;
import com.aurine.cloudx.edge.sync.biz.constant.Constants;
import com.aurine.cloudx.edge.sync.biz.mapper.ProjectRelationMapper;
import com.aurine.cloudx.edge.sync.biz.service.ProjectRelationService;
import com.aurine.cloudx.edge.sync.biz.service.ScribeCallbackService;
import com.aurine.cloudx.edge.sync.common.entity.po.ProjectRelation;
import com.aurine.cloudx.edge.sync.common.enums.ProjectSourceEnum;
import com.aurine.cloudx.edge.sync.common.enums.SyncTypeEnum;
import com.aurine.cloudx.edge.sync.common.utils.RedisUtil;
import com.aurine.cloudx.open.api.feign.RemoteOpenProjectInfoService;
import com.aurine.cloudx.open.common.entity.vo.ProjectInfoVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author cjw
 * @date 2021-12-23 15:31:43
 */
@Service
@Slf4j
public class ProjectRelationServiceImpl extends ServiceImpl<ProjectRelationMapper, ProjectRelation> implements ProjectRelationService {

    @Resource
    private ProjectRelationMapper projectRelationMapper;

    @Resource
    private RemoteOpenProjectInfoService remoteOpenProjectInfoService;

    @Resource
    private MqttClient mqttClient;

    @Resource
    private ScribeCallbackService scribeCallbackService;

    @Resource
    private RedisTemplate<String, String> redisTemplate;



    @Override
    public ProjectRelation getByProjectCode(String projectCode) {
        return baseMapper.selectOne(new QueryWrapper<ProjectRelation>().lambda()
                .eq(ProjectRelation::getProjectCode, projectCode)
        );
    }

    @Override
    public ProjectRelation getByProjectUUID(String projectUUID) {
        String key = "cloudx-edge-sync-platform-master-biz:ProjectRelation：" + projectUUID;
        String data = redisTemplate.opsForValue().get(key);
        if (StringUtils.isNotEmpty(data)) {
            return JSONObject.parseObject(data, ProjectRelation.class);
        }

        ProjectRelation projectRelation = baseMapper.selectOne(new QueryWrapper<ProjectRelation>().lambda()
                .eq(ProjectRelation::getProjectUUID, projectUUID)
        );
        if (projectRelation != null) {
            redisTemplate.opsForValue().set(key, JSONObject.toJSONString(projectRelation), 10, TimeUnit.MINUTES);
        }

        return projectRelation;
    }

    private Boolean saveRelation(ProjectRelation projectRelation) {
        ProjectRelation byProjectUUID = getByProjectUUID(projectRelation.getProjectUUID());
        // 已经存在的数据不入库
        if (byProjectUUID != null) {
            return false;
        }
        return this.save(projectRelation);
    }

    private Boolean deleteRelation(String projectUUID) {
        return projectRelationMapper.deleteByProjectUUID(projectUUID) > 0;
    }


    @Override
    public List<ProjectRelation> getProjectInfoList() {
        List<ProjectRelation> projectRelationList = new ArrayList<>();
        // 不同端调用接口不一致
        R<List<ProjectInfoVo>> r = remoteOpenProjectInfoService.listCascadeByEdge(Constants.appId);
        if (r.getCode() == 0) {
            if (r.getData() != null) {
                for (ProjectInfoVo obj : r.getData()) {
                    ProjectRelation projectRelation = new ProjectRelation();
                    BeanUtil.copyProperties(obj, projectRelation, "createTime", "updateTime");
                    projectRelation.setProjectCode(obj.getCloudProjectUid());
                    projectRelationList.add(projectRelation);
                }
                return projectRelationList;
            }
        }
        return null;
    }

    /**
     * 新增 projectRelation关系表关系
     * 新增 mqtt 订阅
     * 新增 OpenApi 订阅回调地址
     *
     * @param projectRelation
     */
    @Override
    public Boolean addProjectRelation(ProjectRelation projectRelation) {
        projectRelation.setSeq(null);
        if (projectRelation.getSyncType() != null) {
            // 是否以边缘侧数据为主
            projectRelation.setSyncType(projectRelation.getSyncType() == 1 ? SyncTypeEnum.MAIN.code : SyncTypeEnum.SECONDARY.code);
        }
        // 查询级联记录表获取是否项目为本边缘网关项目
        R<List<ProjectInfoVo>> r = remoteOpenProjectInfoService.listCascadeByMaster(Constants.appId);
        if (r.getCode() == 0) {
            if (r.getData() != null) {
                List<ProjectInfoVo> data = r.getData();
                Integer isLocal = ProjectSourceEnum.UN_LOCAL.code;
                for (ProjectInfoVo projectInfoVo : data) {
                    if (projectInfoVo.getProjectUUID().equals(projectRelation.getProjectUUID())) {
                        isLocal = ProjectSourceEnum.LOCAL.code;;
                    }
                }
                projectRelation.setSource(isLocal);
            }
        }
        // 新增 projectRelation关系表关系
        Boolean saveResult = saveRelation(projectRelation);
        if (saveResult) {
            // 新增 mqtt 订阅
            mqttClient.addSubscribeTopics(projectRelation.getProjectUUID());
            // 新增 redis uuid关系
            RedisUtil.set(Constants.UUID_MAP_PREFIX + projectRelation.getProjectUUID(), projectRelation.getProjectCode());
            log.info("新增uuid为{}的projectInfo信息success", projectRelation.getProjectUUID());
        }
        return saveResult;
    }

    /**
     * 删除 projectRelation关系表关系
     * 删除 mqtt 订阅
     * 删除 OpenApi 订阅回调地址
     *
     * @param projectUUID
     */
    @Override
    public Boolean removeProjectRelation(String projectUUID) {
        // 删除 projectRelation关系表关系
        Boolean deleteResult = deleteRelation(projectUUID);
        if (deleteResult) {
            // 删除 mqtt 订阅
            mqttClient.removeSubscribeTopics(projectUUID);
            // 删除 redis uuid关系
            RedisUtil.expire(projectUUID, 5);
            log.info("删除uuid为{}的projectInfo信息success", projectUUID);
        }
        return deleteResult;
    }

    @Override
    public List<String> getUuidList() {
        return projectRelationMapper.getUuidList();
    }

    @Override
    public List<String> getCodeList() {
        return projectRelationMapper.getCodeList();
    }
}
