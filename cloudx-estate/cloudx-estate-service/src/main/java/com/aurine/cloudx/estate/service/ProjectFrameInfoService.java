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

package com.aurine.cloudx.estate.service;

import com.alibaba.excel.exception.ExcelAnalysisException;
import com.aurine.cloudx.estate.dto.ProjectFrameCountDTO;
import com.aurine.cloudx.estate.entity.ProjectFrameInfo;
import com.aurine.cloudx.estate.vo.ProjectDeviceSelectTreeVo;
import com.aurine.cloudx.estate.vo.ProjectFrameInfoTreeVo;
import com.aurine.cloudx.estate.vo.ProjectFrameInfoVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * 框架
 *
 * @author 王伟
 * @date 2020-05-07 14:00:08
 */
public interface ProjectFrameInfoService extends IService<ProjectFrameInfo> {


    /**
     * 检查是否还有下级节点
     *
     * @param id
     * @return
     */
    boolean checkHaveChild(String id);

    /**
     * 验证楼栋框架是否存在房屋
     *
     * @param buildingId
     * @return
     */
    boolean checkBuildingHaveHouse(String buildingId);

    /**
     * 验证是否重名
     *
     * @param name
     * @param puid
     * @return
     */
    boolean checkExists(String name, String puid);

    /**
     * 验证是否重名
     *
     * @param name
     * @param puid
     * @return
     */
    boolean checkExists(String name, String puid, int level);

    /**
     * 根据框架号验证是否存在
     *
     * @return
     */
    boolean checkExistsByProjectId(String frameCode, int projectId);

    /**
     * 获取当前项目下，某一个层级的框架数量
     *
     * @param level
     * @return
     */
    int countByLevel(int level);

    /**
     * 验证是否重名
     *
     * @param projectFrameInfo
     * @return
     */
    boolean checkExists(ProjectFrameInfo projectFrameInfo);

    /**
     * 根据id查询出单元同级列表
     *
     * @param entityid
     * @return
     * @author: 王良俊
     */
    Map<String, Object> getCurrentListAndPuid(String entityid);

    /**
     * 根据父ID，获取上级节点
     *
     * @param puid
     * @return
     */
    List<ProjectFrameInfo> listByPuid(String puid);

    /**
     * 通过框架号获取框架实例
     * @param frameNo
     * @return
     * @author: 王伟
     * @since：2020-12-23 11:40
     */
    ProjectFrameInfo getByFrameNo(String frameNo);

    /**
     * 根据父ID，获取子节点数量
     *
     * @param puid
     * @return
     */
    int countByPuid(String puid);

    /**
     * 获取当前项目下的框架树
     *
     * @param name 如果name不为空，则根节点名称为name，否则不添加根节点
     * @return
     */
    List<ProjectFrameInfoTreeVo> findTree(String name);


    /**
     * 获取当前项目下的level以上的框架树
     *
     * @param level
     * @return
     */
    List<ProjectFrameInfoTreeVo> findTreeByLevel(Integer level);

    /**
     * 获取当前项目下的楼栋树
     *
     * @param
     * @return
     */
    List<ProjectFrameInfoTreeVo> findBuildingTree();

    /**
     * 根据id获取所有上级组团名
     *
     * @param entityId 组团id
     * @return
     */
    String getFrameNameById(String entityId);

    /**
     * 通过entityid获取其下每一级节点的数量以及最终的住户数量
     *
     * @param entityId
     * @return
     */
    List<ProjectFrameCountDTO> listFrameCountAndPersonCount(String entityId);

    /**
     * 获取上级节点
     *
     * @param entityId
     * @return
     */
    ProjectFrameInfo getParent(String entityId);

    /**
     * <p>
     * 判断房屋地址是否正确
     * </p>
     *
     * @param columnName Excel的列名
     * @param address    房屋地址如：1区-1栋-01单元-1010
     * @throws ExcelAnalysisException Excel异常对象
     * @author: 王良俊
     */
    String checkHouseIsCorrect(String columnName, String address, boolean isGroup) throws ExcelAnalysisException;

    /**
     * 通过框架号和projectId保存或更新框架信息
     *
     * @param frameInfoVo
     * @return
     */
    boolean saveOrUpdateFrameByEntityCode(ProjectFrameInfoVo frameInfoVo);

    List<ProjectDeviceSelectTreeVo> getFrameInfosOnPerson(String uid);

    List<ProjectDeviceSelectTreeVo> getAllFrameInfosOnPerson();

    Integer getCountIndoorByIds(List<String> ids);

    /**
     * 根据框架号获取到所有下级房屋的id
     * @param page
     * @param ids
     * @return
     */
    IPage<String> getCheckedIndoorId(IPage page, List<String> ids);

//    String[] getparentIdArr(String entityId);

}
