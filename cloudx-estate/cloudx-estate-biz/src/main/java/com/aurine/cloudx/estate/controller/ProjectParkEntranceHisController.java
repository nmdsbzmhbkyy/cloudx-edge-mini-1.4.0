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

import com.aurine.cloudx.estate.service.ProjectParkEntranceHisService;
import com.aurine.cloudx.estate.vo.ProjectParkEntranceHisVo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.aurine.cloudx.estate.entity.ProjectParkEntranceHis;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 车场管理
 *
 * @author 黄阳光
 * @date 2020-07-07 15:10:26
 */
@RestController
@AllArgsConstructor
@RequestMapping("/parkEntranceHis" )
@Api(value = "parkEntranceHis", tags = "车场管理管理")
public class ProjectParkEntranceHisController {

    private final ProjectParkEntranceHisService parkEntranceHisService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param projectParkEntranceHisVo 车场管理
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    public R getParkEntranceHisPage(Page page, ProjectParkEntranceHisVo projectParkEntranceHisVo) {
        return R.ok(parkEntranceHisService.page(page, projectParkEntranceHisVo));
    }

    /**
     * 用于场内车辆的分页查询
     * 分页查询
     * @param page 分页对象
     * @param projectParkEntranceHisVo 车场管理
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/getEnterVehiclePage")
    public R getEnterVehiclePage(Page page, ProjectParkEntranceHisVo projectParkEntranceHisVo) {
        return R.ok(parkEntranceHisService.enterVehiclePage(page, projectParkEntranceHisVo));
    }

    /**
     * 通过id查询车场管理
     * @param parkOrderNo uid
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{parkOrderNo}" )
    public R getById(@PathVariable("parkOrderNo" ) String parkOrderNo) {
        return R.ok(parkEntranceHisService.getById(parkOrderNo));
    }

    /**
     * 新增车场管理
     * @param po 车场管理
     * @return R
     */
    @ApiOperation(value = "新增车场管理", notes = "新增车场管理")
    @SysLog("新增车场管理" )
    @PostMapping
    public R save(@RequestBody ProjectParkEntranceHis po) {
        return R.ok(parkEntranceHisService.save(po));
    }

    /**
     * 修改车场管理
     * @param projectParkEntranceHis 车场管理
     * @return R
     */
    @ApiOperation(value = "修改车场管理", notes = "修改车场管理")
    @SysLog("修改车场管理" )
    @PutMapping
    public R updateById(@RequestBody ProjectParkEntranceHis projectParkEntranceHis) {
        return R.ok(parkEntranceHisService.updateById(projectParkEntranceHis));
    }

    /**
     * 通过id删除车场管理
     * @param seq id
     * @return R
     */
    /*@ApiOperation(value = "通过id删除车场管理", notes = "通过id删除车场管理")
    @SysLog("通过id删除车场管理" )
    @DeleteMapping("/{seq}" )
    @PreAuthorize("@pms.hasPermission('estate_parkentrancehis_del')" )
    public R removeById(@PathVariable Integer seq) {
        return R.ok(parkEntranceHisService.removeById(seq));
    }*/

}
