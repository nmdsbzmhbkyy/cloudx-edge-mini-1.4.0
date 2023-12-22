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

import cn.hutool.core.collection.CollectionUtil;
import com.aurine.cloudx.common.core.constant.DataConstants;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.entity.ProjectFeeConf;
import com.aurine.cloudx.estate.entity.ProjectPayAccountConf;
import com.aurine.cloudx.estate.entity.ProjectStaffNotice;
import com.aurine.cloudx.estate.service.ProjectPayAccountConfService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;

import com.pig4cloud.pigx.common.security.annotation.Inner;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 项目收款账号管理
 *
 * @author pigx code generator
 * @date 2021-07-06 08:42:02
 */
@RestController
@AllArgsConstructor
@RequestMapping("/projectPayAccountConf" )
@Api(value = "projectPayAccountConf", tags = "项目收款账号管理管理")
public class ProjectPayAccountConfController {

    private final ProjectPayAccountConfService projectPayAccountConfService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param projectPayAccountConf 项目收款账号管理
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    public R getProjectPayAccountConfPage(Page page, ProjectPayAccountConf projectPayAccountConf) {
        LambdaQueryWrapper<ProjectPayAccountConf> queryWrapper = Wrappers.lambdaQuery(ProjectPayAccountConf.class);
        if (projectPayAccountConf.getMechantName() != null && !"".equals(projectPayAccountConf.getMechantName())) {
            queryWrapper.like(ProjectPayAccountConf::getMechantName, projectPayAccountConf.getMechantName());
        }
        if (projectPayAccountConf.getPayType() != null && !"".equals(projectPayAccountConf.getPayType())) {
            queryWrapper.eq(ProjectPayAccountConf::getPayType, projectPayAccountConf.getPayType());
        }
        if (projectPayAccountConf.getAccountStatus() != null && !"".equals(projectPayAccountConf.getAccountStatus())) {
            queryWrapper.eq(ProjectPayAccountConf::getAccountStatus, projectPayAccountConf.getAccountStatus());
        }
        queryWrapper.orderByDesc(ProjectPayAccountConf::getAccountStatus).orderByDesc(ProjectPayAccountConf::getCreateTime);
        return R.ok(projectPayAccountConfService.page(page, queryWrapper));
    }


    /**
     * 通过id查询项目收款账号管理
     * @param  id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) String id) {

        return R.ok(projectPayAccountConfService.getById(id));
    }

    /**
     * 新增项目收款账号管理
     * @param projectPayAccountConf 项目收款账号管理
     * @return R
     */
    @ApiOperation(value = "新增项目收款账号管理", notes = "新增项目收款账号管理")
    @SysLog("新增项目收款账号管理" )
    @PostMapping
    public R save(@RequestBody ProjectPayAccountConf projectPayAccountConf) {
        return R.ok(projectPayAccountConfService.save(projectPayAccountConf));
    }

    /**
     * 修改项目收款账号管理
     * @param projectPayAccountConf 项目收款账号管理
     * @return R
     */
    @ApiOperation(value = "修改项目收款账号管理", notes = "修改项目收款账号管理")
    @SysLog("修改项目收款账号管理" )
    @PutMapping
    public R updateById(@RequestBody ProjectPayAccountConf projectPayAccountConf) {
        return R.ok(projectPayAccountConfService.updateById(projectPayAccountConf));
    }

    /**
     * 通过id删除项目收款账号管理
     * @param seq id
     * @return R
     */
    @ApiOperation(value = "通过id删除项目收款账号管理", notes = "通过id删除项目收款账号管理")
    @SysLog("通过id删除项目收款账号管理" )
    @DeleteMapping("/{seq}" )
    public R removeById(@PathVariable String seq) {
        String accountStatus = projectPayAccountConfService.getOne(Wrappers.lambdaQuery(ProjectPayAccountConf.class)
                .eq(ProjectPayAccountConf::getAccountId, seq).select(ProjectPayAccountConf::getAccountStatus)).getAccountStatus();
        if(DataConstants.TRUE.equals(accountStatus)) {
            return R.failed("无法删除已启动的配置");
        }

        return R.ok(projectPayAccountConfService.removeById(seq));
    }
    @PutMapping("/off/{id}")
    @ApiOperation(value = "关闭收费账号设置", notes = "关闭收费账号设置")
    public R setOff(@PathVariable("id") String id) {

        return R.ok(this.projectPayAccountConfService
                .update(Wrappers.lambdaUpdate(ProjectPayAccountConf.class)
                        .set(ProjectPayAccountConf::getAccountStatus, DataConstants.FALSE)
                        .eq(ProjectPayAccountConf::getAccountId, id)));
    }

    /**
     * 启用收费设置
     *
     * @param id 主键
     * @return 修改结果
     */
    @PutMapping("/on/{id}")
    @ApiOperation(value = "启用收费账号设置", notes = "启用收费账号设置")
    public R setOn(@PathVariable("id")String id) {
        ProjectPayAccountConf byId = projectPayAccountConfService.getById(id);
        List<ProjectPayAccountConf> list = this.projectPayAccountConfService.list(Wrappers.lambdaQuery(ProjectPayAccountConf.class).eq(ProjectPayAccountConf::getAccountStatus, DataConstants.TRUE)
                .eq(ProjectPayAccountConf::getPayType, byId.getPayType()));
        if (CollectionUtil.isNotEmpty(list)) {
            return R.failed("当前已有同类型收款账号，请禁用后，再启用");
        }
        return R.ok(this.projectPayAccountConfService
                .update(Wrappers.lambdaUpdate(ProjectPayAccountConf.class)
                        .set(ProjectPayAccountConf::getAccountStatus, DataConstants.TRUE)
                        .eq(ProjectPayAccountConf::getAccountId, id)));
    }
    @ApiOperation(value = "商户名称同名校验", notes = "商户名称同名校验")
    @SysLog("新增项目收款账号管理" )
    @GetMapping("/getMechantNameByProject")
    public R getMechantNameByProject(ProjectPayAccountConf projectPayAccountConf){
        LambdaQueryWrapper<ProjectPayAccountConf> queryWrapper = Wrappers.lambdaQuery(ProjectPayAccountConf.class);
        queryWrapper.eq(ProjectPayAccountConf::getMechantName , projectPayAccountConf.getMechantName())
                .eq(ProjectPayAccountConf::getPayType , projectPayAccountConf.getPayType())
                .eq(ProjectPayAccountConf::getProjectId , projectPayAccountConf.getProjectId());
        List<ProjectPayAccountConf> list = projectPayAccountConfService.list(queryWrapper);
        return R.ok(list);
    }


    /**
     * 通过id查询项目收款账号管理
     * @param  projectId
     * @return R
     */
    @Inner
    @ApiOperation(value = "通过项目查询", notes = "通过项目查询")
    @GetMapping("/getAccountByProjectId/{projectId}" )
    public R getAccountByProjectId(@PathVariable("projectId" ) Integer projectId) {
        ProjectContextHolder.setProjectId(projectId);
        ProjectPayAccountConf one = projectPayAccountConfService.getOne(new LambdaQueryWrapper<ProjectPayAccountConf>()
                .eq(ProjectPayAccountConf::getProjectId, projectId).eq(ProjectPayAccountConf::getAccountStatus,DataConstants.TRUE)
        .eq(ProjectPayAccountConf::getPayType,"2"));
        return R.ok(one);
    }

}
