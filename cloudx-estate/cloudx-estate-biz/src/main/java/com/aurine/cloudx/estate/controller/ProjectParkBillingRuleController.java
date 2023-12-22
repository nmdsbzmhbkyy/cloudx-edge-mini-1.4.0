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

package com.aurine.cloudx.estate.controller;

import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.common.core.constant.DataConstants;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.ParkingRuleTypeEnum;
import com.aurine.cloudx.estate.entity.ProjectParkBillingRule;
import com.aurine.cloudx.estate.service.ProjectParkBillingRuleService;
import com.aurine.cloudx.estate.vo.ProjectParkBillingRuleSearchConditionVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 车场计费规则
 *
 * @author 王伟
 * @date 2020-07-07 11:34:12
 */
@RestController
@AllArgsConstructor
@RequestMapping("/projectParkingBillRule")
@Api(value = "projectParkingBillRule", tags = "车场计费规则管理")
public class ProjectParkBillingRuleController {

    private final ProjectParkBillingRuleService projectParkBillingRuleService;

    /**
     * 分页查询
     *
     * @param page              分页对象
     * @param searchConditionVo 车场计费规则
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R getProjectParkBillingRulePage(Page page, ProjectParkBillingRuleSearchConditionVo searchConditionVo) {
        return R.ok(projectParkBillingRuleService.pageBillRule(page, searchConditionVo));
    }

    /**
     * 查询可用的计费规则
     *
     * @param parkId 停车场ID
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/listUseable/{parkId}")
    public R listUseable(@PathVariable("parkId") String parkId) {
        List<ProjectParkBillingRule> list = projectParkBillingRuleService.list(
                new QueryWrapper<ProjectParkBillingRule>()
                        .lambda()
                        .eq(ProjectParkBillingRule::getParkId, parkId)
                        .eq(ProjectParkBillingRule::getProjectId, ProjectContextHolder.getProjectId())
                        .eq(ProjectParkBillingRule::getIsDisable, DataConstants.FALSE)
                        .notLike(ProjectParkBillingRule::getRuleType, ParkingRuleTypeEnum.TEMP.code)
        );
        return R.ok(list);
    }


    /**
     * 通过id查询车场计费规则
     *
     * @param ruleId ruleId
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{ruleId}")
    public R getById(@PathVariable("ruleId") String ruleId) {
        return R.ok(projectParkBillingRuleService.getById(ruleId));
    }

    /**
     * 新增车场计费规则
     *
     * @param projectParkBillingRule 车场计费规则
     * @return R
     */
    @ApiOperation(value = "新增车场计费规则", notes = "新增车场计费规则")
    @SysLog("新增车场计费规则")
    @PostMapping
//    @PreAuthorize("@pms.hasPermission('estate_projectparkbillingrule_add')")
    public R save(@RequestBody ProjectParkBillingRule projectParkBillingRule) {
        projectParkBillingRule.setProjectId(ProjectContextHolder.getProjectId());
        return R.ok(projectParkBillingRuleService.save(projectParkBillingRule));
    }

    /**
     * 修改车场计费规则
     *
     * @param projectParkBillingRule 车场计费规则
     * @return R
     */
    @ApiOperation(value = "修改车场计费规则", notes = "修改车场计费规则")
    @SysLog("修改车场计费规则")
    @PutMapping
//    @PreAuthorize("@pms.hasPermission('estate_projectparkbillingrule_edit')")
    public R updateById(@RequestBody ProjectParkBillingRule projectParkBillingRule) {
        return R.ok(projectParkBillingRuleService.updateById(projectParkBillingRule));
    }

    /**
     * 通过id删除车场计费规则
     *
     * @param ruleId ruleId
     * @return R
     */
    @ApiOperation(value = "通过id删除车场计费规则", notes = "通过id删除车场计费规则")
    @SysLog("通过id删除车场计费规则")
    @DeleteMapping("/{ruleId}")
//    @PreAuthorize("@pms.hasPermission('estate_projectparkbillingrule_del')")
    public R removeById(@PathVariable String ruleId) {
        return R.ok(projectParkBillingRuleService.removeById(ruleId));
    }


    /**
     * 获取本项目所有计费规则列表
     *
     * @return R
     */
    @ApiOperation(value = "获取本项目所有计费规则列表", notes = "获取本项目所有计费规则列表")
    @SysLog("获取本项目所有计费规则列表")
    @GetMapping("/getAllRuleMap")
    public R getAllRuleMap() {
        List<ProjectParkBillingRule> list = projectParkBillingRuleService.list(new QueryWrapper<ProjectParkBillingRule>().lambda()
                .eq(ProjectParkBillingRule::getProjectId, ProjectContextHolder.getProjectId()));
        if (CollUtil.isNotEmpty(list)) {
            return R.ok(list.stream().collect(Collectors.toMap(ProjectParkBillingRule::getRuleId, ProjectParkBillingRule::getRuleName)));
        }
        return R.ok(new HashMap<String, String>());
    }

}
