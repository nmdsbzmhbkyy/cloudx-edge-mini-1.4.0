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
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.aurine.cloudx.common.core.util.TreeUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.FrameTypeEnum;
import com.aurine.cloudx.estate.dto.ProjectFrameCountDTO;
import com.aurine.cloudx.estate.entity.ProjectFrameInfo;
import com.aurine.cloudx.estate.mapper.ProjectFrameInfoMapper;
import com.aurine.cloudx.estate.service.ProjectEntityLevelCfgService;
import com.aurine.cloudx.estate.service.ProjectFrameInfoService;
import com.aurine.cloudx.estate.vo.ProjectDeviceSelectTreeVo;
import com.aurine.cloudx.estate.vo.ProjectFrameInfoTreeVo;
import com.aurine.cloudx.estate.vo.ProjectFrameInfoVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 框架
 *
 * @author 王伟
 * @date 2020-05-07 14:00:08
 */
@Service
public class ProjectFrameInfoServiceImpl extends ServiceImpl<ProjectFrameInfoMapper, ProjectFrameInfo> implements ProjectFrameInfoService {

    @Resource
    ProjectEntityLevelCfgService projectEntityLevelCfgService;

    @Resource
    ProjectFrameInfoService projectFrameInfoService;

    /**
     * 检查是否还有下级节点
     *
     * @param id
     * @return
     */
    @Override
    public boolean checkHaveChild(String id) {
        int count = this.count(new QueryWrapper<ProjectFrameInfo>().eq("puid", id));
        return count > 0 ? true : false;
    }

    /**
     * 验证楼栋框架是否存在房屋
     *
     * @param buildingId
     * @return
     */
    @Override
    public boolean checkBuildingHaveHouse(String buildingId) {
        List<ProjectFrameInfo> unitList = list(new QueryWrapper<ProjectFrameInfo>().lambda().eq(ProjectFrameInfo::getPuid, buildingId));

        if (CollectionUtil.isEmpty(unitList)) {//不存在单元，直接返回ture
            return false;
        } else {
            List<String> idList = unitList.stream().map(ProjectFrameInfo::getEntityId).collect(Collectors.toList());
            int houseCount = count(new QueryWrapper<ProjectFrameInfo>().lambda().in(ProjectFrameInfo::getPuid, idList));
            return houseCount >= 1;
        }

    }

    /**
     * 验证是否重名
     *
     * @param name
     * @param puid
     * @return
     */
    @Override
    public boolean checkExists(String name, String puid) {
        int count = this.count(new QueryWrapper<ProjectFrameInfo>().eq("entityName", name).eq(StringUtils.isNotEmpty(puid), "puid", puid));
        return count > 0 ? true : false;
    }

    /**
     * 验证是否重名
     *
     * @param name
     * @param puid
     * @param level
     * @return
     */
    @Override
    public boolean checkExists(String name, String puid, int level) {
        int count = this.count(new QueryWrapper<ProjectFrameInfo>()
                .eq("entityName", name)
                .eq(StringUtils.isNotEmpty(puid), "puid", puid)
                .eq("level", level)
        );
        return count > 0 ? true : false;
    }

    /**
     * 根据框架号验证是否存在
     *
     * @param frameCode
     * @param projectId
     * @return
     */
    @Override
    public boolean checkExistsByProjectId(String frameCode, int projectId) {
        return baseMapper.countByProjectIdAndCode(frameCode, projectId) >= 1;
    }

    /**
     * 获取当前项目下，某一个层级的框架数量
     *
     * @param level
     * @return
     */
    @Override
    public int countByLevel(int level) {
        return this.count(new QueryWrapper<ProjectFrameInfo>().lambda().eq(ProjectFrameInfo::getLevel, level));
    }

    /**
     * 验证是否重名
     *
     * @param projectFrameInfo
     * @return
     */
    @Override
    public boolean checkExists(ProjectFrameInfo projectFrameInfo) {
        return checkExists(projectFrameInfo.getEntityName(), projectFrameInfo.getPuid());
    }

    @Override
    public Map<String, Object> getCurrentListAndPuid(String entityid) {
        List<ProjectFrameInfo> frameInfoList = this.list(new QueryWrapper<ProjectFrameInfo>().lambda()
                .eq(ProjectFrameInfo::getEntityId, entityid));
        if (CollUtil.isNotEmpty(frameInfoList)) {
            String puid = frameInfoList.get(0).getPuid();
            List<ProjectFrameInfo> list = this.list(new QueryWrapper<ProjectFrameInfo>().lambda()
                    .eq(ProjectFrameInfo::getPuid, puid));
            Map<String, Object> map = new HashMap<>();
            map.put("list", list);
            map.put("puid", puid);
            return map;
        }
        return new HashMap<>();
    }

    @Override
    public List<ProjectFrameInfo> listByPuid(String puid) {
        this.list(new QueryWrapper<ProjectFrameInfo>().lambda().eq(ProjectFrameInfo::getPuid, puid));
        return baseMapper.listByPuid(puid);
    }

    /**
     * 通过框架号获取框架实例
     *
     * @param frameNo
     * @return
     * @author: 王伟
     * @since：2020-12-23 11:40
     */
    @Override
    public ProjectFrameInfo getByFrameNo(String frameNo) {
        List<ProjectFrameInfo> frameInfoList = this.list(new QueryWrapper<ProjectFrameInfo>().lambda().eq(ProjectFrameInfo::getEntityCode, frameNo));
        if (CollUtil.isNotEmpty(frameInfoList)) {
            return frameInfoList.get(0);
        }
        return null;
    }

    /**
     * 根据父ID，获取子节点数量
     *
     * @param puid
     * @return
     */
    @Override
    public int countByPuid(String puid) {
        return this.count(new QueryWrapper<ProjectFrameInfo>().lambda().eq(ProjectFrameInfo::getPuid, puid));
    }

//    /**
//     * 根据父ID，获取上级节点
//     *
//     * @param puid
//     * @return
//     */
//    @Override
//    public ProjectFrameInfo getByPuid(String puid) {
//        return this.getOne(new QueryWrapper<ProjectFrameInfo>().lambda().eq(ProjectFrameInfo::getPuid,puid));
//    }

    /**
     * 获取当前项目下的框架树
     *
     * @param name 如果name不为空，则根节点名称为name，否则不添加根节点
     * @return
     */
    @Override
    public List<ProjectFrameInfoTreeVo> findTree(String name) {

        int maxLevel = 0;
        List<ProjectFrameInfo> projectFrameInfoList = this.list(new QueryWrapper<ProjectFrameInfo>().lambda().gt(ProjectFrameInfo::getLevel, FrameTypeEnum.BUILDING.code).orderByAsc(ProjectFrameInfo::getCreateTime));

        String root = "1";
        for (ProjectFrameInfo frame : projectFrameInfoList) {
            if (StringUtils.isEmpty(frame.getPuid())) {
                frame.setPuid("1");
            }
        }

        //如果存在根节点命名，则创建一个根节点
        if (StringUtils.isNotEmpty(name)) {


            //定义根节点
            ProjectFrameInfo frameInfo = new ProjectFrameInfo();
            frameInfo.setLevel(8);
            frameInfo.setEntityName(name);
            frameInfo.setPuid("0");
            frameInfo.setEntityId("1");
            frameInfo.setCreateTime(LocalDateTime.now());
            projectFrameInfoList.add(frameInfo);

            root = "0";
        }


        return getTree(projectFrameInfoList, root);
    }

    @Override
    public List<ProjectFrameInfoTreeVo> findTreeByLevel(Integer level) {
        List<ProjectFrameInfo> projectFrameInfoList = this.list(new QueryWrapper<ProjectFrameInfo>().lambda().gt(ProjectFrameInfo::getLevel, FrameTypeEnum.BUILDING.code).orderByAsc(ProjectFrameInfo::getCreateTime));

        String root = "1";
        for (ProjectFrameInfo frame : projectFrameInfoList) {
            if (StringUtils.isEmpty(frame.getPuid())) {
                frame.setPuid("1");
            }
        }
        projectFrameInfoList.addAll(projectFrameInfoService.list(new QueryWrapper<ProjectFrameInfo>()
                .lambda().eq(ProjectFrameInfo::getLevel, 3)
                .or().eq(ProjectFrameInfo::getLevel, 2)
                .or().eq(ProjectFrameInfo::getLevel, 1)));
        return getTreeByLevel(projectFrameInfoList, root, level);
    }

    /**
     * 获取当前项目下的楼栋树
     *
     * @return
     */
    @Override
    public List<ProjectFrameInfoTreeVo> findBuildingTree() {
        int maxLevel = 0;
        List<ProjectFrameInfo> projectFrameInfoList =
                baseMapper.getByLevel(FrameTypeEnum.BUILDING.code);
        String root = "1";
        for (ProjectFrameInfo frame : projectFrameInfoList) {
            if (StringUtils.isEmpty(frame.getPuid())) {
                frame.setPuid("1");
            }
        }

        //将楼栋的puid设置为1，以关联上根节点
        projectFrameInfoList.forEach(e -> {
            if (e.getLevel() == 3) {
                e.setPuid("1");
            }
        });

        //定义根节点
        ProjectFrameInfo frameInfo = new ProjectFrameInfo();
        frameInfo.setLevel(4);
        frameInfo.setEntityName("小区");
        frameInfo.setPuid("0");
        frameInfo.setEntityId("1");
        frameInfo.setCreateTime(LocalDateTime.now());
        projectFrameInfoList.add(frameInfo);

        root = "0";

        return getTree(projectFrameInfoList, root);
    }

    @Override
    public String getFrameNameById(String entityId) {
        if (projectEntityLevelCfgService.checkIsEnabled()) {
            return baseMapper.getFrameInfoById(entityId);
        } else {
            return "";
        }
    }

    /**
     * 通过entityid获取其下每一级节点的数量以及最终的住户数量
     *
     * @param entityId
     * @return
     */
    @Override
    public List<ProjectFrameCountDTO> listFrameCountAndPersonCount(String entityId) {
        return this.baseMapper.listFrameAndPersonCounts(entityId, TenantContextHolder.getTenantId(), ProjectContextHolder.getProjectId());
    }

    /**
     * 获取上级节点
     *
     * @param entityId
     * @return
     */
    @Override
    public ProjectFrameInfo getParent(String entityId) {
        ProjectFrameInfo curr = getById(entityId);
        return curr == null ? null : getById(curr.getPuid());
    }

    @Override
    public String checkHouseIsCorrect(String columnName, String address, boolean isGroup) throws ExcelAnalysisException {

        List<String> houseIdList = new ArrayList<>();
        if (isGroup) {
            houseIdList = baseMapper.getHouseIdByAddressGroup(address);
        } else {
            houseIdList = baseMapper.getHouseIdByAddressNotGroup(address);
        }
        if (CollUtil.isNotEmpty(houseIdList)) {
            return houseIdList.get(0);
        }
        throw new ExcelAnalysisException("楼栋/单元/房屋填写错误 系统无法找到对应房屋");
    }

    /**
     * 保存或更新框架信息（同步框架）
     *
     * @param frameInfoVo
     * @return
     */
    @Override
    public boolean saveOrUpdateFrameByEntityCode(ProjectFrameInfoVo frameInfoVo) {

        //根据第三方id查询是否存在框架数据
        ProjectFrameInfoVo frameInfo = baseMapper.getByCode(frameInfoVo.getEntityCode(), frameInfoVo.getProjectId());

        if (frameInfo == null) {
            return this.baseMapper.saveFrame(frameInfoVo) >= 1;
        } else {
            frameInfoVo.setEntityId(frameInfo.getEntityId());
            frameInfoVo.setPuid(null);
            return this.updateById(frameInfoVo);
        }
    }


    /**
     * 构建结构树
     *
     * @param projectFrameList
     * @return
     */
    private List<ProjectFrameInfoTreeVo> getTree(List<ProjectFrameInfo> projectFrameList, String root) {
        List<ProjectFrameInfoTreeVo> treeList = projectFrameList.stream()
                .filter(node -> !node.getEntityId().equals(node.getPuid()))
                .sorted(Comparator.comparing(ProjectFrameInfo::getCreateTime))
                .map(frameInfo -> {
                    ProjectFrameInfoTreeVo node = new ProjectFrameInfoTreeVo();
                    node.setId(frameInfo.getEntityId());
                    node.setParentId(frameInfo.getPuid());
                    node.setName(frameInfo.getEntityName());
                    node.setLevel(frameInfo.getLevel());
                    node.setCode(frameInfo.getEntityCode());
                    return node;
                }).collect(Collectors.toList());

        return TreeUtil.build(treeList, root);
    }

    /**
     * 构建结构树
     *
     * @param projectFrameList
     * @return
     */
    private List<ProjectFrameInfoTreeVo> getTreeByLevel(List<ProjectFrameInfo> projectFrameList, String root, Integer level) {
        List<ProjectFrameInfoTreeVo> treeList = projectFrameList.stream()
                .filter(node -> !node.getEntityId().equals(node.getPuid()) && node.getLevel() >= level)
                .sorted(Comparator.comparing(ProjectFrameInfo::getCreateTime))
                .map(frameInfo -> {
                    ProjectFrameInfoTreeVo node = new ProjectFrameInfoTreeVo();
                    node.setId(frameInfo.getEntityId());
                    node.setParentId(frameInfo.getPuid());
                    node.setName(frameInfo.getEntityName());
                    node.setLevel(frameInfo.getLevel());
                    node.setCode(frameInfo.getEntityCode());
                    return node;
                }).collect(Collectors.toList());

        return TreeUtil.build(treeList, root);
    }

    /**
     * 根据父级id获取楼栋单元与存在人屋关系的房屋
     * @param uid
     * @return
     */
    @Override
    public List<ProjectDeviceSelectTreeVo> getFrameInfosOnPerson(String uid) {
        return baseMapper.getFrameInfoByPerson(uid);
    }

    /**
     * 获取所有楼栋单元与存在人屋关系的房屋
     * @param
     * @return
     */
    @Override
    public List<ProjectDeviceSelectTreeVo> getAllFrameInfosOnPerson() {
        return baseMapper.getAllFrameInfosOnPerson( );
    }

    @Override
    public Integer getCountIndoorByIds(List<String> ids) {
        return baseMapper.getCountByIndoor(ids);
    }

    @Override
    public IPage<String> getCheckedIndoorId(IPage page,List<String> ids) {
        return baseMapper.getCheckedIndoorId(page,ids);
    }


}
