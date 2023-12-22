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
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.ParkingAPICompanyEnum;
import com.aurine.cloudx.estate.constant.enums.ParkingRuleTypeEnum;
import com.aurine.cloudx.estate.entity.ProjectParkBillingRule;
import com.aurine.cloudx.estate.entity.ProjectParkingInfo;
import com.aurine.cloudx.estate.mapper.ProjectParkBillingRuleMapper;
import com.aurine.cloudx.estate.service.ProjectParkBillingRuleService;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.parking.platform.fujica.entity.constant.CardTypeEnum;
import com.aurine.cloudx.estate.thirdparty.module.parking.platform.fujica.entity.constant.FujicaConstant;
import com.aurine.cloudx.estate.thirdparty.module.parking.platform.fujica.remote.factory.FujicaRemoteParkingServiceFactory;
import com.aurine.cloudx.estate.vo.ProjectParkBillingRuleRecordVo;
import com.aurine.cloudx.estate.vo.ProjectParkBillingRuleSearchConditionVo;
import com.aurine.cloudx.estate.vo.ProjectParkRuleVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 车场计费规则
 *
 * @author 王伟
 * @date 2020-07-07 11:34:12
 */
@Service
public class ProjectParkBillingRuleServiceImpl extends ServiceImpl<ProjectParkBillingRuleMapper, ProjectParkBillingRule> implements ProjectParkBillingRuleService {

    @Resource
    RedisTemplate redisTemplateAurine;

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
    public boolean initByCompany(String parkId, String company, Integer projectId,ProjectParkingInfo parkingInfo) {

//        List<ProjectParkBillingRule> parkBillingRuleList =  ParkingFactoryProducer.getFactory(parkId).getParkingService().getBillingRuleList(parkId, projectId);
        List<ProjectParkBillingRule> parkBillingRuleList = new ArrayList<>();
        //赛菲姆的初始化规则数据
        if (ParkingAPICompanyEnum.SFIRM.value.equals(company)) {

            parkBillingRuleList.add(createBean("3651", "临时车A", parkId, ParkingRuleTypeEnum.TEMP, projectId));
            parkBillingRuleList.add(createBean("3650", "临时车B", parkId, ParkingRuleTypeEnum.TEMP, projectId));
            parkBillingRuleList.add(createBean("3649", "临时车C", parkId, ParkingRuleTypeEnum.TEMP, projectId));
            parkBillingRuleList.add(createBean("3648", "临时车D", parkId, ParkingRuleTypeEnum.TEMP, projectId));
            parkBillingRuleList.add(createBean("3652", "月租车A", parkId, ParkingRuleTypeEnum.MONTH, projectId));
            parkBillingRuleList.add(createBean("3653", "月租车B", parkId, ParkingRuleTypeEnum.MONTH, projectId));
            parkBillingRuleList.add(createBean("3654", "月租车C", parkId, ParkingRuleTypeEnum.MONTH, projectId));
            parkBillingRuleList.add(createBean("3655", "月租车D", parkId, ParkingRuleTypeEnum.MONTH, projectId));
            parkBillingRuleList.add(createBean("3661", "月租车E", parkId, ParkingRuleTypeEnum.MONTH, projectId));
            parkBillingRuleList.add(createBean("3662", "月租车F", parkId, ParkingRuleTypeEnum.MONTH, projectId));
            parkBillingRuleList.add(createBean("3663", "月租车G", parkId, ParkingRuleTypeEnum.MONTH, projectId));
            parkBillingRuleList.add(createBean("3664", "月租车H", parkId, ParkingRuleTypeEnum.MONTH, projectId));
            parkBillingRuleList.add(createBean("3656", "免费车", parkId, ParkingRuleTypeEnum.FREE, projectId));
//            parkBillingRuleList.add(createBean("3657", "储值车", parkId,ParkingRuleTypeEnum.TEMP));
//            parkBillingRuleList.add(createBean("3660", "超时收费", parkId,ParkingRuleTypeEnum.TEMP));
            return this.saveBatch(parkBillingRuleList);
        } else if (ParkingAPICompanyEnum.FUJICA.value.equals(company)) {

            JSONObject resultJson = FujicaRemoteParkingServiceFactory.getInstance(VersionEnum.V1).getPayType(parkingInfo.getParkCode(), FujicaConstant.APP_ID, parkingInfo.getProjectId(), null);

            if (resultJson == null) {
                log.error("[富士车场] 未获取到缴费规则数据");
                throw new RuntimeException("未获取到缴费规则数据");
            }

            JSONArray resultArray = resultJson.getJSONArray("Records");
            if (CollUtil.isEmpty(resultArray)) {
                log.error("[富士车场] 未获取到缴费规则数据");
                throw new RuntimeException("未获取到缴费规则数据");
            }

            boolean haveFreeRule = false;
            for (JSONObject recode : resultArray.toJavaList(JSONObject.class)) {

                //仅存储首个免费车
                if (haveFreeRule && CardTypeEnum.getCloudCodeByFujica(recode.getString("TcmType")) == ParkingRuleTypeEnum.FREE) {
                    continue;
                }

                if (CardTypeEnum.getCloudCodeByFujica(recode.getString("TcmType")) == ParkingRuleTypeEnum.FREE) {
                    haveFreeRule = true;
                }

                parkBillingRuleList.add(createBean(recode.getString("Rid"), recode.getString("Name"), parkId, CardTypeEnum.getCloudCodeByFujica(recode.getString("TcmType")), projectId));
            }

            return this.saveBatch(parkBillingRuleList);
        }else if (ParkingAPICompanyEnum.REFORMER.value.equals(company)){
            parkBillingRuleList.add(createBean("3651", "临时车A", parkId, ParkingRuleTypeEnum.TEMP, projectId));
            parkBillingRuleList.add(createBean("3652", "月租车A", parkId, ParkingRuleTypeEnum.MONTH, projectId));
            parkBillingRuleList.add(createBean("3656", "免费车", parkId, ParkingRuleTypeEnum.FREE, projectId));
            return this.saveBatch(parkBillingRuleList);
        }

        return this.saveBatch(parkBillingRuleList);
//        return false;

    }


    /**
     * 构造停车收费规则
     *
     * @param ruleCode
     * @param ruleName
     * @return
     */
    private ProjectParkBillingRule createBean(String ruleCode, String ruleName, String parkId, ParkingRuleTypeEnum ruleTypeEnum, Integer projectId) {
        ProjectParkBillingRule rule = new ProjectParkBillingRule();
        rule.setRuleCode(ruleCode);
        rule.setRuleName(ruleName);
        rule.setRuleName(ruleName);
        rule.setIsDisable(ruleTypeEnum == ParkingRuleTypeEnum.MONTH ? "1" : "0");
        rule.setProjectId(projectId);
        rule.setParkId(parkId);
        rule.setRuleType(ruleTypeEnum.code);
        return rule;
    }

    @Override
    public String getRuleIdByRuleName(String redisKey, String parkId, String ruleName) {
        if (redisTemplateAurine.hasKey(redisKey)) {
            return (String) redisTemplateAurine.opsForHash().get(redisKey, parkId + ruleName);
        } else {
            this.initRuleTmpCache(redisKey);
            return getRuleIdByRuleName(redisKey, parkId, ruleName);
        }
    }

    @Override
    public void deleteParkingRuleTmpCache(String redisKey) {
        redisTemplateAurine.delete(redisKey);
    }

    private void initRuleTmpCache(String redisKey) {
        List<ProjectParkRuleVo> parkRuleList = this.baseMapper.getRuleParkList(ProjectContextHolder.getProjectId());
        Map<String, String> parkRuleMap;
        if (CollUtil.isNotEmpty(parkRuleList)) {
            parkRuleMap = parkRuleList.stream().filter(projectParkRuleVo -> StrUtil.isNotEmpty(projectParkRuleVo.getParkingRuleKey()))
                    .collect(Collectors.toMap(ProjectParkRuleVo::getParkingRuleKey, ProjectParkRuleVo::getRuleId, (s, s2) -> s2));
        } else {
            parkRuleMap = new HashMap<>();
            parkRuleMap.put("1", "1");
        }
        redisTemplateAurine.delete(redisKey);
        redisTemplateAurine.opsForHash().putAll(redisKey, parkRuleMap);
        redisTemplateAurine.expire(redisKey, 2, TimeUnit.HOURS);
    }


}
