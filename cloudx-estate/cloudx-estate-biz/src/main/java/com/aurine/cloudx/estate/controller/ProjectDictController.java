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

import com.aurine.cloudx.common.core.constant.DataConstants;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.entity.ProjectDict;
import com.aurine.cloudx.estate.entity.ProjectFeeConf;
import com.aurine.cloudx.estate.service.ProjectDictService;
import com.aurine.cloudx.estate.service.ProjectFeeConfService;
import com.aurine.cloudx.estate.vo.ProjectDictVo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;

import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


/**
 * 项目字典表
 *
 * @author cjw
 * @date 2021-07-07 08:38:37
 */
@RestController
@AllArgsConstructor
@RequestMapping("/projectDict" )
@Api(value = "projectDict", tags = "项目字典表管理")
public class ProjectDictController {

    private final ProjectDictService projectDictService;

    @Resource
    private ProjectFeeConfService projectFeeConfService;
    /**
     * 分页查询
     * @param page 分页对象
     * @param projectDict 项目字典表
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    public R getProjectDictPage(Page page, ProjectDict projectDict) {
        return R.ok(projectDictService.page(page, Wrappers.query(projectDict)));
    }


    /**
     * 通过id查询项目字典表
     * @param seq id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{seq}" )
    public R getById(@PathVariable("seq" ) Integer seq) {
        return R.ok(projectDictService.getById(seq));
    }

    /**
     * 新增项目字典表
     * @param projectDict 项目字典表
     * @return R
     */
    @ApiOperation(value = "新增项目字典表", notes = "新增项目字典表")
    @SysLog("新增项目字典表" )
    @PostMapping
    public R save(@RequestBody ProjectDict projectDict) {
        return R.ok(projectDictService.save(projectDict));
    }

    /**
     * 修改项目字典表
     * @param projectDict 项目字典表
     * @return R
     */
    @ApiOperation(value = "修改项目字典表", notes = "修改项目字典表")
    @SysLog("修改项目字典表" )
    @PutMapping
    public R updateById(@RequestBody ProjectDict projectDict) {
        return R.ok(projectDictService.updateById(projectDict));
    }

    /**
     * 通过id删除项目字典表
     * @param seq id
     * @return R
     */
    @ApiOperation(value = "通过id删除项目字典表", notes = "通过id删除项目字典表")
    @SysLog("通过id删除项目字典表" )
    @DeleteMapping("/{seq}" )
    public R removeById(@PathVariable Integer seq) {
        return R.ok(projectDictService.removeById(seq));
    }

    @ApiOperation(value = "获取费用类型列表", notes = "获取费用类型列表")
    @GetMapping("/feeTypePage")
    public R getFeeTypeList(Page page) {
        ProjectDictVo projectDictVo = new ProjectDictVo();
        projectDictVo.setDictType("fee_type");
        projectDictVo.setProjectId(ProjectContextHolder.getProjectId());
        return R.ok(projectDictService.getFeeTypeList(page, projectDictVo));
    }
    @ApiOperation(value = "获取费用类型下拉框", notes = "获取费用类型下拉框")
    @GetMapping("/feeTypeActive")
    public R feeTypeActive() {
        List<ProjectDictVo> projectDictVos = projectDictService.feeTypeActiveList();
        return R.ok(projectDictVos);
    }

    @ApiOperation(value = "修改费用类型", notes = "修改费用类型")
    @PostMapping("/updateFeeType")
    public R updateFeeType(@RequestBody ProjectDictVo projectDictVo) {
        // 如果是启动的状态
        if (DataConstants.TRUE.equals(projectDictVo.getStatus())) {
            Integer count = projectDictService.count(Wrappers.lambdaQuery(ProjectDict.class)
                    .eq(ProjectDict::getDictValue, projectDictVo.getDictValue()).eq(ProjectDict::getDictCode, projectDictVo.getDictCode())
                    .eq(ProjectDict::getProjectId, ProjectContextHolder.getProjectId()));
            if (count > 0) {
                projectDictService.update(projectDictVo,Wrappers.lambdaQuery(ProjectDict.class).eq(ProjectDict::getDictValue, projectDictVo.getDictValue()).eq(ProjectDict::getDictCode, projectDictVo.getDictCode())
                        .eq(ProjectDict::getProjectId,ProjectContextHolder.getProjectId()));
                this.projectFeeConfService
                        .update(Wrappers.lambdaUpdate(ProjectFeeConf.class)
                                .set(ProjectFeeConf::getStatus, DataConstants.TRUE)
                                .eq(ProjectFeeConf::getFeeType, projectDictVo.getDictCode()));
            }
        } else {
            // 如果是禁用的状态
            Integer count = projectDictService.count(Wrappers.lambdaQuery(ProjectDict.class)
                    .eq(ProjectDict::getDictValue, projectDictVo.getDictValue()).eq(ProjectDict::getDictCode, projectDictVo.getDictCode())
                    .eq(ProjectDict::getProjectId,ProjectContextHolder.getProjectId()));
            if (count > 0) {
                // 如果存在当条记录
                projectDictService.update(projectDictVo,Wrappers.lambdaQuery(ProjectDict.class).eq(ProjectDict::getDictValue, projectDictVo.getDictValue()).eq(ProjectDict::getDictCode, projectDictVo.getDictCode())
                        .eq(ProjectDict::getProjectId,ProjectContextHolder.getProjectId()));
                this.projectFeeConfService
                        .update(Wrappers.lambdaUpdate(ProjectFeeConf.class)
                                .set(ProjectFeeConf::getStatus, DataConstants.FALSE)
                                .eq(ProjectFeeConf::getFeeType, projectDictVo.getDictCode()));
            } else {
                // 不存在的话 往里面新增一条记录
                projectDictVo.setProjectId(ProjectContextHolder.getProjectId());
                projectDictService.save(projectDictVo);
            }
        }
        return R.ok();
    }
    @ApiOperation(value = "新增费用类型", notes = "新增费用类型")
    @PostMapping("/insertFeeType")
    public R insertFeeType(@RequestBody ProjectDictVo projectDictVo) {

        return projectDictService.insertFeeType(projectDictVo);

    }

}
