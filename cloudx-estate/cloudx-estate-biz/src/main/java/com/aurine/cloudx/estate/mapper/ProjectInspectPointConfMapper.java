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

package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.vo.ProjectInspectPointConfSearchConditionVo;
import com.aurine.cloudx.estate.vo.ProjectInspectPointConfVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aurine.cloudx.estate.entity.ProjectInspectPointConf;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备巡检点配置
 *
 * @author 王良俊
 * @date 2020-07-23 16:26:33
 */
@Mapper
public interface ProjectInspectPointConfMapper extends BaseMapper<ProjectInspectPointConf> {

    /**
     * <p>
     * 分页查询数据
     * </p>
     *
     * @param page  分页对象
     * @param query 查询条件
     * @return 分页数据
     */
    Page<ProjectInspectPointConfVo> fetchList(Page<ProjectInspectPointConfVo> page, @Param("query") ProjectInspectPointConfSearchConditionVo query);

    /**
     * <p>
     * 只获取到状态为启用的巡检点分页数据（用于路线添加巡检点时选择使用）
     * </p>
     *
     * @param page  分页对象
     * @param query 查询条件
     * @return 分页数据
     */
    Page<ProjectInspectPointConfVo> fetchListInSelectPage(Page<ProjectInspectPointConfVo> page, @Param("query") ProjectInspectPointConfSearchConditionVo query);

    /**
     * <p>
     * 只获取到状态为启用的巡检点分页数据（用于路线添加巡检点时选择使用）
     * </p>
     *
     * @param inspectRouteId 巡检路线ID
     * @return 巡检点vo对象列表
     */
    List<ProjectInspectPointConfVo> listInspectionByRouteId(@Param("inspectRouteId") String inspectRouteId);

}
