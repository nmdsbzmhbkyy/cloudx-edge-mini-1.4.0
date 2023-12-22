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

import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aurine.cloudx.estate.entity.ProjectParkBillingInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 缴费记录
 *
 * @author 黄阳光
 * @date 2020-07-10 09:49:12
 */
@Mapper
public interface ProjectParkBillingInfoMapper extends BaseMapper<ProjectParkBillingInfo> {

    IPage<ProjectParkBillingInfoVo> selectPage(Page page, @Param("param") ProjectParkBillingInfoVo vo, @Param("projectId") Integer projectId);

    ProjectParkBillingTotalSearchConditionVo selectIncome(@Param("param") ProjectParkBillingTotalSearchConditionVo vo);

    IPage<ProjectParkBillingSearchConditionVo> selectAllBilling(Page page, @Param("param") ProjectParkBillingTotalSearchConditionVo vo);

    String selectByParkCode(@Param("parkId") String parkId, @Param("projectId") Integer projectId);

    ProjectParkBillingSourceSearchTotalConditionVo selectSourceIncome(@Param("param") ProjectParkBillingSourceSearchTotalConditionVo vo);

    IPage<ProjectParkBillingSourceSearchConditionVo> selectBySource(Page page, @Param("param") ProjectParkBillingSourceSearchTotalConditionVo vo);

    /**
     * 获取当月总金额
     * @param tenantId
     * @param projectId
     * @return
     */
    BigDecimal currMonthPayment(@Param("tenantId") Integer tenantId, @Param("projectId") Integer projectId);

    /**
     * 获取指定月份总金额
     * @param date
     * @param tenantId
     * @param projectId
     * @return
     */
    BigDecimal monthPayment(@Param("date") LocalDateTime date, @Param("tenantId") Integer tenantId, @Param("projectId") Integer projectId);

    /**
     * 获取指定天的金额
     * @param date
     * @param tenantId
     * @param projectId
     * @return
     */
    BigDecimal dayPayment(@Param("date") LocalDateTime date, @Param("tenantId") Integer tenantId, @Param("projectId") Integer projectId);
}
