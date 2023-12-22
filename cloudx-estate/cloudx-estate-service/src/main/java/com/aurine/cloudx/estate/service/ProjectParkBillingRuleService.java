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

import com.aurine.cloudx.estate.constant.enums.ParkingAPICompanyEnum;
import com.aurine.cloudx.estate.entity.ProjectParkBillingRule;
import com.aurine.cloudx.estate.vo.ProjectParkBillingRuleRecordVo;
import com.aurine.cloudx.estate.vo.ProjectParkBillingRuleSearchConditionVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.lettuce.core.dynamic.annotation.Param;

/**
 * 车场计费规则
 *
 * @author 王伟
 * @date 2020-07-07 11:34:12
 */
public interface ProjectParkBillingRuleService extends IService<ProjectParkBillingRule> {

    /**
     * 分页查询信息
     *
     * @param searchConditionVo
     * @return
     */
    Page<ProjectParkBillingRuleRecordVo> pageBillRule(Page<ProjectParkBillingRuleRecordVo> page, @Param("query") ProjectParkBillingRuleSearchConditionVo searchConditionVo);


    /**
     * 根据厂商初始化车场计费规则
     *
     * @param company
     */
    boolean initByCompany(String parkId, String company, Integer projectId);

}
