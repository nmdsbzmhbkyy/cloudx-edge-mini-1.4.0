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

import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.ParkingAPICompanyEnum;
import com.aurine.cloudx.estate.constant.enums.ParkingRuleTypeEnum;
import com.aurine.cloudx.estate.entity.ProjectParkBillingRule;
import com.aurine.cloudx.estate.mapper.ProjectParkBillingRuleMapper;
import com.aurine.cloudx.estate.service.ProjectParkBillingRuleService;
import com.aurine.cloudx.estate.vo.ProjectParkBillingRuleRecordVo;
import com.aurine.cloudx.estate.vo.ProjectParkBillingRuleSearchConditionVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 车场计费规则
 *
 * @author 王伟
 * @date 2020-07-07 11:34:12
 */
@Service
public class ProjectParkBillingRuleServiceImpl extends ServiceImpl<ProjectParkBillingRuleMapper, ProjectParkBillingRule> implements ProjectParkBillingRuleService {

    /**
     * 分页查询信息
     *
     * @param page
     * @param searchConditionVo
     * @return
     */
    @Override
    public Page<ProjectParkBillingRuleRecordVo> pageBillRule(Page<ProjectParkBillingRuleRecordVo> page, ProjectParkBillingRuleSearchConditionVo searchConditionVo) {
        return this.baseMapper.pageBillRule(page, searchConditionVo, ProjectContextHolder.getProjectId());
    }

    /**
     * 根据厂商初始化车场计费规则
     *
     * @param company
     */
    @Override
    public boolean initByCompany(String parkId, String company, Integer projectId) {

        //赛菲姆的初始化规则数据
        if (ParkingAPICompanyEnum.SFIRM.value.equals(company)) {
            List<ProjectParkBillingRule> parkBillingRuleList = new ArrayList<>();
            parkBillingRuleList.add(createBean("3651", "临时车A", parkId,ParkingRuleTypeEnum.TEMP, projectId));
            parkBillingRuleList.add(createBean("3650", "临时车B", parkId,ParkingRuleTypeEnum.TEMP, projectId));
            parkBillingRuleList.add(createBean("3649", "临时车C", parkId,ParkingRuleTypeEnum.TEMP, projectId));
            parkBillingRuleList.add(createBean("3648", "临时车D", parkId,ParkingRuleTypeEnum.TEMP, projectId));
            parkBillingRuleList.add(createBean("3652", "月租车A", parkId,ParkingRuleTypeEnum.MONTH, projectId));
            parkBillingRuleList.add(createBean("3653", "月租车B", parkId,ParkingRuleTypeEnum.MONTH, projectId));
            parkBillingRuleList.add(createBean("3654", "月租车C", parkId,ParkingRuleTypeEnum.MONTH, projectId));
            parkBillingRuleList.add(createBean("3655", "月租车D", parkId,ParkingRuleTypeEnum.MONTH, projectId));
            parkBillingRuleList.add(createBean("3661", "月租车E", parkId,ParkingRuleTypeEnum.MONTH, projectId));
            parkBillingRuleList.add(createBean("3662", "月租车F", parkId,ParkingRuleTypeEnum.MONTH, projectId));
            parkBillingRuleList.add(createBean("3663", "月租车G", parkId,ParkingRuleTypeEnum.MONTH, projectId));
            parkBillingRuleList.add(createBean("3664", "月租车H", parkId,ParkingRuleTypeEnum.MONTH, projectId));
            parkBillingRuleList.add(createBean("3656", "免费车", parkId,ParkingRuleTypeEnum.FREE, projectId));
//            parkBillingRuleList.add(createBean("3657", "储值车", parkId,ParkingRuleTypeEnum.TEMP));
//            parkBillingRuleList.add(createBean("3660", "超时收费", parkId,ParkingRuleTypeEnum.TEMP));
            return this.saveBatch(parkBillingRuleList);
        }
        return false;

    }


    /**
     * 构造停车收费规则
     *
     * @param ruleCode
     * @param ruleName
     * @return
     */
    private ProjectParkBillingRule createBean(String ruleCode, String ruleName, String parkId,ParkingRuleTypeEnum ruleTypeEnum, Integer projectId) {
        ProjectParkBillingRule rule = new ProjectParkBillingRule();
        rule.setRuleCode(ruleCode);
        rule.setRuleName(ruleName);
        rule.setRuleName(ruleName);
        rule.setIsDisable(ruleTypeEnum==ParkingRuleTypeEnum.MONTH ? "1" : "0");
        rule.setProjectId(projectId);
        rule.setParkId(parkId);
        rule.setRuleType(ruleTypeEnum.code);
        return rule;
    }
}
