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

import com.aurine.cloudx.estate.vo.ProjectInspectPointConfSearchConditionVo;
import com.aurine.cloudx.estate.vo.ProjectInspectPointConfVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aurine.cloudx.estate.entity.ProjectInspectPointConf;

import java.util.List;

/**
 * 设备巡检点配置
 *
 * @author 王良俊
 * @date 2020-07-23 16:26:33
 */
public interface ProjectInspectPointConfService extends IService<ProjectInspectPointConf> {

    /**
     * <p>
     * 分页查询
     * </p>
     *
     * @param page  分页数据
     * @param query 查询条件
     * @return 分页数据
     */
    Page<ProjectInspectPointConfVo> fetchList(Page<ProjectInspectPointConfVo> page, ProjectInspectPointConfSearchConditionVo query);

    /**
     * <p>
     * 根据路线id获取该路线绑定的巡检点列表
     * </p>
     *
     * @param inspectRouteId 巡检路线id
     * @return 巡检点vo对象列表
     */
    List<ProjectInspectPointConfVo> listByInspectPointByRouteId(String inspectRouteId);

    /**
     * <p>
     * 保存巡检点
     * </p>
     *
     * @param projectInspectPointConfVo 巡检点vo对象
     * @return 处理结果
     */
    boolean saveInspectPoint(ProjectInspectPointConfVo projectInspectPointConfVo);

    /**
     * <p>
     * 删除巡检点
     * </p>
     *
     * @param pointId 巡检点id
     * @return 处理结果
     */
    boolean removeInspectPoint(String pointId);

    /**
     * <p>
     * 更新巡检点信息
     * </p>
     *
     * @param projectInspectPointConfVo 巡检点vo对象
     * @return 处理结果
     */
    boolean updateInspectPoint(ProjectInspectPointConfVo projectInspectPointConfVo);

}
