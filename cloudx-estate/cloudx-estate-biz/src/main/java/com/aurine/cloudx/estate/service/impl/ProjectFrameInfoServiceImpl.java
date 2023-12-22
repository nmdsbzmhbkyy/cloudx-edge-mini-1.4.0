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
import com.aurine.cloudx.estate.entity.ProjectBuildingInfo;
import com.aurine.cloudx.estate.entity.ProjectFrameInfo;
import com.aurine.cloudx.estate.entity.ProjectHouseInfo;
import com.aurine.cloudx.estate.entity.ProjectUnitInfo;
import com.aurine.cloudx.estate.mapper.ProjectFrameInfoMapper;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
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
    ProjectBuildingInfoService projectBuildingInfoService;

    @Resource
    ProjectUnitInfoService projectUnitInfoService;

    @Resource
    ProjectHouseInfoService projectHouseInfoService;

    @Resource
    RedisTemplate<String, Object> redisTemplate;


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
     * 验证组团编码是否重复
     *
     * @param code
     * @param puid
     * @return
     */
    @Override
    public boolean checkExistsCode(String code, String puid) {
        int count1 = this.count(new QueryWrapper<ProjectFrameInfo>().eq("entityCode", code).eq(StringUtils.isNotEmpty(puid), "puid", puid));
        return count1 > 0 ? true : false;
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
     * 验证组团编码是否重复
     *
     * @param code
     * @param puid
     * @param level
     * @return
     */
    @Override
    public boolean checkExistsCode(String code, String puid, int level) {
        int count1 = this.count(new QueryWrapper<ProjectFrameInfo>()
                .eq("entityCode", code)
                .eq(StringUtils.isNotEmpty(puid), "puid", puid)
                .eq("level", level)
        );
        return count1 > 0 ? true : false;
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

    /**
     * 验证组团编码是否重复
     *
     * @param projectFrameInfo
     * @return
     */
    @Override
    public boolean checkExistsCode(ProjectFrameInfo projectFrameInfo) {
        return checkExistsCode(projectFrameInfo.getEntityCode(), projectFrameInfo.getPuid());
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
        return baseMapper.getFrameInfoById(entityId);
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
    public R<String> checkHouseIsCorrect(String address, boolean isGroup) throws ExcelAnalysisException {
        List<String> houseIdList;
        if (isGroup) {
            houseIdList = baseMapper.getHouseIdByAddressGroup(address);
        } else {
            houseIdList = baseMapper.getHouseIdByAddressNotGroup(address);
        }
        if (CollUtil.isNotEmpty(houseIdList)) {
            return R.ok(houseIdList.get(0));
        }
        return R.failed();
    }

    @Override
    public String getBuildingId(String buildingName) {
        return baseMapper.getBuildingId(buildingName);
    }

    @Override
    public List<ProjectHouseAddressVo> getHouseAddressInfoList(Integer projectId) {
        ProjectContextHolder.setProjectId(projectId);
        if (projectEntityLevelCfgService.checkIsEnabled()) {
            return this.baseMapper.getHouseIDAndAddressInfoGroup(projectId);
        } else {
            return this.baseMapper.getHouseIDAndAddressInfo(projectId);
        }
    }

    @Override
    public String checkHouseIsCorrect(String batchId, String address) {
        String houseId = null;
        if (StringUtils.isNotEmpty(address)) {
            if (redisTemplate.hasKey(batchId)) {
                houseId = (String) redisTemplate.opsForHash().get(batchId, address);
            } else {
                Map<String, String> addressMap;
                List<ProjectHouseAddressVo> projectHouseAddressVos = this.getHouseAddressInfoList(ProjectContextHolder.getProjectId());
                if (CollUtil.isNotEmpty(projectHouseAddressVos)) {
                    addressMap = projectHouseAddressVos.stream().filter(projectHouseAddressVo -> StringUtils.isNotEmpty(projectHouseAddressVo.getAddress()))
                            .collect(Collectors.toMap(ProjectHouseAddressVo::getAddress, ProjectHouseAddressVo::getHouseId, (val1, val2) -> val2));
                } else {
                    addressMap = new HashMap<>();
                    addressMap.put("1", "1");
                }
                redisTemplate.opsForHash().putAll(batchId, addressMap);
                // 这里先默认保留2小时
                redisTemplate.expire(batchId, 2, TimeUnit.HOURS);
                houseId = addressMap.get(address);
            }
        }
        return houseId;
    }

    @Override
    public void deleteHouseAddressCache(String batchId) {
        redisTemplate.delete(batchId);
    }

    /**
     * 保存或更新框架信息（同步框架）
     *
     * @param frameInfoVo
     * @return
     */
    @Override
    public String saveOrUpdateFrameByEntityCode(ProjectFrameInfoVo frameInfoVo) {

        //根据第三方id查询是否存在框架数据
        ProjectFrameInfoVo frameInfo = baseMapper.getByCode(frameInfoVo.getEntityCode(), frameInfoVo.getProjectId());

        if (frameInfo == null) {
//            return this.baseMapper.saveFrame(frameInfoVo) >= 1;
            this.baseMapper.saveFrame(frameInfoVo);
            return frameInfoVo.getEntityId();
        } else {
            frameInfoVo.setEntityId(frameInfo.getEntityId());
            frameInfoVo.setPuid(null);
//            return this.updateById(frameInfoVo);
            this.updateById(frameInfoVo);

            return frameInfo.getEntityId();
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
                    //node.setCode(frameInfo.getEntityCode());
                    node.setCode(frameInfo.getFrameNo());
                    return node;
                }).collect(Collectors.toList());

        return TreeUtil.build(treeList, root);
    }

    /**
     * 根据父级id获取楼栋单元与存在人屋关系的房屋
     *
     * @param uid
     * @return
     */
    @Override
    public List<ProjectDeviceSelectTreeVo> getFrameInfosOnPerson(String uid) {
        return baseMapper.getFrameInfoByPerson(uid);
    }

    /**
     * 获取所有楼栋单元与存在人屋关系的房屋
     *
     * @param
     * @return
     */
    @Override
    public List<ProjectDeviceSelectTreeVo> getAllFrameInfosOnPerson() {
        return baseMapper.getAllFrameInfosOnPerson();
    }

    @Override
    public Integer getCountIndoorByIds(List<String> ids) {
        return baseMapper.getCountByIndoor(ids);
    }

    @Override
    public IPage<String> getCheckedIndoorId(IPage page, List<String> ids) {
        return baseMapper.getCheckedIndoorId(page, ids);
    }

    /**
     * 根据实例id，自下而上获取上层全部节点的名称组合
     * DEMO：组团7-组团6-组团5-组团4-楼栋-单元-房屋
     *
     * @param entityId
     * @param separator 连接符
     * @return
     */
    @Override
    public String getFrameNameByEntityId(String entityId, String separator, Integer beginLevel, Integer endLevel) {

        if (StringUtils.isEmpty(entityId)) {
            return null;
        }

        if (separator == null) {
            separator = "-";
        }

        return baseMapper.getFullFrameNameByEntityId(entityId, separator, beginLevel, endLevel);
    }

    @Override
    public List<String> listHouseNameByBuildingId(String buildingId) {
        return baseMapper.listHouseNameByBuildingId(buildingId);
    }

    /**
     * 添加框架
     *
     * @param projectFrameInfo
     * @return
     */
    @Override
    public Boolean saveFrameInfo(ProjectFrameInfo projectFrameInfo) {
        boolean flag = false;
        Integer level = projectFrameInfo.getLevel();

        if (4 == level) {
            flag = saveLevel(projectFrameInfo, flag);
        }
        if (5 == level) {
            flag = saveLevel(projectFrameInfo, flag);

        }
        if (6 == level) {
            flag = saveLevel(projectFrameInfo, flag);

        }
        if (7 == level) {
            flag = save(projectFrameInfo);
        }

        return flag;
    }

    /**
     * 添加框架层级
     *
     * @param projectFrameInfo
     * @param flag
     * @return
     */
    private Boolean saveLevel(ProjectFrameInfo projectFrameInfo, boolean flag) {
        List<ProjectFrameInfo> projectFrameInfoList = list(new QueryWrapper<ProjectFrameInfo>().eq("entityId", projectFrameInfo.getPuid()));
        if (CollectionUtil.isNotEmpty(projectFrameInfoList)) {
            for (ProjectFrameInfo frameInfo : projectFrameInfoList) {
                projectFrameInfo.setEntityCode(frameInfo.getEntityCode() + projectFrameInfo.getFrameNo());
                flag = save(projectFrameInfo);
            }
        } else {
            flag = save(projectFrameInfo);
        }
        return flag;
    }

    /**
     * 修改框架编码
     *
     * @param projectFrameInfo
     * @return
     */
    @Override
    public Boolean updateFrameNoByCode(ProjectFrameInfo projectFrameInfo) {
        boolean flag = false;

        String code = null;
        Integer level = projectFrameInfo.getLevel();

        if (4 == level) {
            ProjectFrameInfo one = getOne(new QueryWrapper<ProjectFrameInfo>().eq("entityId", projectFrameInfo.getPuid()));
            //修改四级组团
            if (one != null) {
                projectFrameInfo.setFrameNo(projectFrameInfo.getFrameNo());
                projectFrameInfo.setEntityCode(one.getEntityCode() + projectFrameInfo.getFrameNo());
                flag = updateById(projectFrameInfo);
            } else {
                projectFrameInfo.setFrameNo(projectFrameInfo.getFrameNo());
                projectFrameInfo.setEntityCode(projectFrameInfo.getFrameNo());
                flag = updateById(projectFrameInfo);
            }

            flag = this.updateBuildingFrameByEntityId(projectFrameInfo, projectFrameInfo.getEntityCode());

        }
        if (5 == level) {
            ProjectFrameInfo one = getOne(new QueryWrapper<ProjectFrameInfo>().eq("entityId", projectFrameInfo.getPuid()));
            //修改五级组团
            if (one != null) {
                projectFrameInfo.setFrameNo(projectFrameInfo.getFrameNo());
                projectFrameInfo.setEntityCode(one.getEntityCode() + projectFrameInfo.getFrameNo());
                flag = updateById(projectFrameInfo);
            } else {
                projectFrameInfo.setFrameNo(projectFrameInfo.getFrameNo());
                projectFrameInfo.setEntityCode(projectFrameInfo.getFrameNo());
                flag = updateById(projectFrameInfo);
            }

            List<ProjectFrameInfo> projectFrameInfoList = list(new QueryWrapper<ProjectFrameInfo>().eq("puid", projectFrameInfo.getEntityId()));
            if (CollectionUtil.isNotEmpty(projectFrameInfoList)) {
                for (ProjectFrameInfo frameInfo : projectFrameInfoList) {
                    code = projectFrameInfo.getEntityCode() + frameInfo.getFrameNo();
                    frameInfo.setEntityCode(code);
                    //修改六级组团
                    updateById(frameInfo);
                    flag = this.updateBuildingFrameByEntityId(frameInfo, frameInfo.getEntityCode());

                }
            }

        }
        if (6 == level) {
            ProjectFrameInfo one = getOne(new QueryWrapper<ProjectFrameInfo>().eq("entityId", projectFrameInfo.getPuid()));
            //修改六级组团
            if (one != null) {
                projectFrameInfo.setFrameNo(projectFrameInfo.getFrameNo());
                projectFrameInfo.setEntityCode(one.getEntityCode() + projectFrameInfo.getFrameNo());
                flag = updateById(projectFrameInfo);
            } else {
                projectFrameInfo.setFrameNo(projectFrameInfo.getFrameNo());
                projectFrameInfo.setEntityCode(projectFrameInfo.getFrameNo());
                flag = updateById(projectFrameInfo);
            }


            List<ProjectFrameInfo> projectFrameInfoList = list(new QueryWrapper<ProjectFrameInfo>().eq("puid", projectFrameInfo.getEntityId()));
            if (CollectionUtil.isNotEmpty(projectFrameInfoList)) {
                for (ProjectFrameInfo frameInfo : projectFrameInfoList) {
                    code = projectFrameInfo.getEntityCode() + frameInfo.getFrameNo();
                    frameInfo.setEntityCode(code);
                    //修改五级组团
                    flag = updateById(frameInfo);

                    List<ProjectFrameInfo> projectFrameInfoList1 = list(new QueryWrapper<ProjectFrameInfo>().eq("puid", frameInfo.getEntityId()));
                    if (CollectionUtil.isNotEmpty(projectFrameInfoList1)) {
                        for (ProjectFrameInfo frameInfo1 : projectFrameInfoList1) {
                            code = frameInfo.getEntityCode() + frameInfo1.getFrameNo();
                            frameInfo1.setEntityCode(code);
                            //修改四级组团
                            updateById(frameInfo1);
                            flag = this.updateBuildingFrameByEntityId(frameInfo1, frameInfo1.getEntityCode());
                        }
                    }

                }
            }

        }
        if (7 == level) {
            //修改七级组团
            projectFrameInfo.setFrameNo(projectFrameInfo.getFrameNo());
            projectFrameInfo.setEntityCode(projectFrameInfo.getFrameNo());
            flag = updateById(projectFrameInfo);

            List<ProjectFrameInfo> projectFrameInfoList = list(new QueryWrapper<ProjectFrameInfo>().eq("puid", projectFrameInfo.getEntityId()));
            if (CollectionUtil.isNotEmpty(projectFrameInfoList)) {
                for (ProjectFrameInfo frameInfo : projectFrameInfoList) {
                    code = projectFrameInfo.getEntityCode() + frameInfo.getFrameNo();
                    frameInfo.setEntityCode(code);
                    //修改六级组团
                    flag = updateById(frameInfo);

                    List<ProjectFrameInfo> projectFrameInfoList1 = list(new QueryWrapper<ProjectFrameInfo>().eq("puid", frameInfo.getEntityId()));
                    if (CollectionUtil.isNotEmpty(projectFrameInfoList1)) {
                        for (ProjectFrameInfo frameInfo1 : projectFrameInfoList1) {
                            code = projectFrameInfo.getFrameNo() + frameInfo.getFrameNo() + frameInfo1.getFrameNo();
                            frameInfo1.setEntityCode(code);
                            //修改五级组团
                            flag = updateById(frameInfo1);

                            List<ProjectFrameInfo> projectFrameInfoList2 = list(new QueryWrapper<ProjectFrameInfo>().eq("puid", frameInfo1.getEntityId()));
                            if (CollectionUtil.isNotEmpty(projectFrameInfoList2)) {
                                for (ProjectFrameInfo frameInfo2 : projectFrameInfoList2) {
                                    code = projectFrameInfo.getFrameNo() + frameInfo.getFrameNo() + frameInfo1.getFrameNo() + frameInfo2.getFrameNo();
                                    frameInfo2.setEntityCode(code);
                                    //修改四级组团
                                    updateById(frameInfo2);
                                    flag = this.updateBuildingFrameByEntityId(frameInfo2, frameInfo2.getEntityCode());
                                }
                            }
                        }
                    }

                }
            }

        }
        return flag;
    }

    /**
     * 修改楼栋编码
     *
     * @param building
     * @param entityCode
     * @return
     */
    private Boolean updateBuildingFrameByEntityId(ProjectFrameInfo building, String entityCode) {
        boolean flag = false;
        List<ProjectFrameInfo> projectFrameInfooList = list(new QueryWrapper<ProjectFrameInfo>().eq("puid", building.getEntityId()));
        for (ProjectFrameInfo projectFrameInfo : projectFrameInfooList) {
            String code = entityCode + projectFrameInfo.getFrameNo();
            projectFrameInfo.setEntityCode(code);
            //修改楼栋框架编码
            updateById(projectFrameInfo);
            List<ProjectBuildingInfo> projectBuildingInfoList = projectBuildingInfoService.list(new QueryWrapper<ProjectBuildingInfo>().eq("buildingId", projectFrameInfo.getEntityId()));
            if (CollectionUtil.isNotEmpty(projectBuildingInfoList)) {
                for (ProjectBuildingInfo projectBuildingInfo : projectBuildingInfoList) {
                    projectBuildingInfo.setBuildingCode(code);
                    //修改楼栋编码
                    flag = projectBuildingInfoService.updateById(projectBuildingInfo);
                    if (flag) {
                        //修改单元编码
                        //查询单元框架信息
                        List<ProjectFrameInfo> projectFrameInfoList = list(new QueryWrapper<ProjectFrameInfo>().eq("puid", projectFrameInfo.getEntityId()));
                        flag = updateUnitFrameByEntityId(projectFrameInfoList, code);
                    }
                }
            }
        }
        return flag;
    }

    /**
     * 修改单元编码
     *
     * @param projectFrameInfoList
     * @param code
     * @return
     */
    private Boolean updateUnitFrameByEntityId(List<ProjectFrameInfo> projectFrameInfoList, String code) {
        boolean flag = false;
        for (ProjectFrameInfo projectFrameInfo : projectFrameInfoList) {
            String codes = code + projectFrameInfo.getFrameNo();
            projectFrameInfo.setEntityCode(codes);
            //修改单元框架编码
            updateById(projectFrameInfo);
            List<ProjectUnitInfo> projectUnitInfoList = projectUnitInfoService.list(new QueryWrapper<ProjectUnitInfo>().eq("unitId", projectFrameInfo.getEntityId()));
            for (ProjectUnitInfo projectUnitInfo : projectUnitInfoList) {
                //修改楼栋编码
                projectUnitInfo.setUnitCode(codes);
                flag = projectUnitInfoService.updateById(projectUnitInfo);
                if (flag) {
                    //修改房屋编码
                    //查询房屋框架信息
                    List<ProjectFrameInfo> projectHouseInfoList = list(new QueryWrapper<ProjectFrameInfo>().eq("puid", projectFrameInfo.getEntityId()));
                    flag = updateHouseFrameByEntityId(projectHouseInfoList, codes);
                }
            }
        }
        return flag;
    }

    /**
     * 修改房屋编码
     *
     * @param projectFrameInfoList
     * @param code
     * @return
     */
    private Boolean updateHouseFrameByEntityId(List<ProjectFrameInfo> projectFrameInfoList, String code) {
        for (ProjectFrameInfo projectFrameInfo : projectFrameInfoList) {
            String codes = code + projectFrameInfo.getFrameNo();
            projectFrameInfo.setEntityCode(codes);
            //修改房屋框架编码
            updateById(projectFrameInfo);
            List<ProjectHouseInfo> projectHouseInfoList = projectHouseInfoService.list(new QueryWrapper<ProjectHouseInfo>().eq("houseId", projectFrameInfo.getEntityId()));
            for (ProjectHouseInfo projectHouseInfo : projectHouseInfoList) {
                //修改房屋编码
                projectHouseInfo.setHouseCode(codes);
                boolean bool = projectHouseInfoService.updateById(projectHouseInfo);
                if (!bool) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 统计项目的楼栋数
     *
     * @param projectId
     * @return
     */
    @Override
    public Integer countFrameInfo(Integer projectId) {
        return baseMapper.countFrameInfo(projectId);
    }

    @Override
    public String getPuidByEntityId(String entityId) {
        ProjectFrameInfo projectFrameInfo = this.getOne(new QueryWrapper<ProjectFrameInfo>().eq("entityId", entityId));
        if (projectFrameInfo != null) {
            return projectFrameInfo.getPuid();
        }
        return null;
    }

    @Override
    public String getFrameIdByName(String redisKey, String frameName) {
        if (!redisTemplate.hasKey(redisKey)) {
            synchronized (this) {
                if (!redisTemplate.hasKey(redisKey)) {
                    List<FrameInfo> frameInfoList = baseMapper.getAllFrameInfo();
                    if (CollUtil.isNotEmpty(frameInfoList)) {
                        Map<String, String> frameInfoMap = frameInfoList.stream().collect(Collectors.toMap(FrameInfo::getFrameName, FrameInfo::getEntityId, (s, s2) -> s2));
                        frameInfoMap.put("1", "1");
                        redisTemplate.opsForHash().putAll(redisKey, frameInfoMap);
                        redisTemplate.expire(redisKey, 1, TimeUnit.HOURS);
                    }
                }
            }
        }
        return (String) redisTemplate.opsForHash().get(redisKey, frameName);
    }

    @Override
    public String getHouseFullNameByHouseId(String houseId) {
        return baseMapper.getHouseFullNameByHouseId(houseId);
    }

}
