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


import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.DecidedEnum;
import com.aurine.cloudx.estate.entity.SysDecided;
import com.aurine.cloudx.estate.service.SysDecidedService;
import com.aurine.cloudx.estate.util.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 订阅信息控制层(sys_decided)
 *
 * @author lgx
 * @date 2021-07-28 15:29:36
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/sysDecided")
@Api(value = "sysDecided", tags = "订阅信息管理")
public class SysDecidedController {

    private final SysDecidedService sysDecidedService;


    /**
     * 已订阅的数据分页查询
     *
     * @param page
     * @param sysDecided
     * @return
     */

    @ApiOperation(value = "已订阅的数据分页查询", notes = "已订阅的数据分页查询")
    @GetMapping("/page")
    public R getSysDecidedPage(Page page, SysDecided sysDecided) {
//        sysDecidedService.page(page, Wrappers.query(sysDecided));
        return R.ok(sysDecidedService.page(page, new LambdaQueryWrapper<SysDecided>()
                .eq(SysDecided::getProjectid, ProjectContextHolder.getProjectId())
                .eq(SysDecided::getUserid, SecurityUtils.getUser().getId())));
    }


    /**
     * 订阅
     *
     * @param sysDecided
     * @return R
     */
    @ApiOperation(value = "订阅", notes = "订阅")
    @SysLog("订阅")
    @PostMapping("/subscription")
    public R subscription(@RequestBody SysDecided sysDecided) {
        sysDecided.setUserid(SecurityUtils.getUser().getId());
        sysDecided.setProjectid(ProjectContextHolder.getProjectId());
        if (sysDecided.getAddr() == null) {
            return R.ok("订阅地址不能为空");
        }
        return R.ok(sysDecidedService.subscription(sysDecided));
    }

    /**
     * 修改
     *
     * @param sysDecided
     * @return R
     */
    @ApiOperation(value = "修改", notes = "修改")
    @SysLog("修改")
    @PutMapping
//    @PreAuthorize("@pms.hasPermission('generator_sysdecided_edit')" )
    public R updateById(@RequestBody SysDecided sysDecided) {
        RedisUtil.del("subscribe:" + DecidedEnum.getEnum(sysDecided.getType()) + ":" + sysDecided.getProjectid());
        return R.ok(sysDecidedService.updateById(sysDecided));
    }

    /**
     * 通过id删除
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除", notes = "通过id删除")
    @SysLog("通过id删除")
    @DeleteMapping("/{id}")
//    @PreAuthorize("@pms.hasPermission('generator_sysdecided_del')" )
    public R removeById(@PathVariable Integer id) {
        SysDecided sysDecided = sysDecidedService.getOne(new LambdaQueryWrapper<SysDecided>().eq(SysDecided::getId, id));
        if (sysDecided != null) {
            RedisUtil.del("subscribe:" + DecidedEnum.getEnum(sysDecided.getType()) + ":" + sysDecided.getProjectid());
        }
        return R.ok(sysDecidedService.removeById(id));
    }

}
