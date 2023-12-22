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

import com.aurine.cloudx.estate.entity.ProjectParkBillingInfo;
import com.aurine.cloudx.estate.service.ProjectParkBillingInfoService;
import com.aurine.cloudx.estate.vo.ProjectParkBillingInfoVo;
import com.aurine.cloudx.estate.vo.ProjectParkBillingSourceSearchTotalConditionVo;
import com.aurine.cloudx.estate.vo.ProjectParkBillingTotalSearchConditionVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


/**
 * 缴费记录
 *
 * @author 黄阳光
 * @date 2020-07-10 09:49:12
 */
@RestController
@AllArgsConstructor
@RequestMapping("/parkBillingInfo")
@Api(value = "parkBillingInfo", tags = "缴费记录管理")
public class ProjectParkBillingInfoController {

    private final ProjectParkBillingInfoService parkBillingInfoService;

    /**
     * 分页查询
     *
     * @param page 分页对象
     * @param vo   缴费记录
     * @return
     */
    @ApiOperation(value = "缴费记录分页查询", notes = "缴费记录分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @GetMapping("/page")
    public R getParkBillingInfoPage(Page page, ProjectParkBillingInfoVo vo) {
        return R.ok(parkBillingInfoService.page(page, vo));
    }


    /**
     * 通过id查询缴费记录
     * @param payOrderNo id
     * @return R
     */
    /*@ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{payOrderNo}" )
    public R getById(@PathVariable("payOrderNo" ) String payOrderNo) {
        return R.ok(parkBillingInfoService.getById(payOrderNo));
    }*/

    /**
     * 新增缴费记录
     *
     * @param projectParkBillingInfo 缴费记录
     * @return R
     */
    @ApiOperation(value = "新增缴费记录", notes = "新增缴费记录")
    @SysLog("新增缴费记录")
    @PostMapping
    public R save(@RequestBody ProjectParkBillingInfo projectParkBillingInfo) {
        return R.ok(parkBillingInfoService.save(projectParkBillingInfo));
    }

    /**
     * 修改缴费记录
     *
     * @param projectParkBillingInfo 缴费记录
     * @return R
     */
    @ApiOperation(value = "修改缴费记录", notes = "修改缴费记录")
    @SysLog("修改缴费记录")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('estate_parkbillinginfo_edit')")
    public R updateById(@RequestBody ProjectParkBillingInfo projectParkBillingInfo) {
        return R.ok(parkBillingInfoService.updateById(projectParkBillingInfo));
    }

    /**
     * 通过id删除缴费记录
     *
     * @param seq id
     * @return R
     */
    @ApiOperation(value = "通过id删除缴费记录", notes = "通过id删除缴费记录")
    @SysLog("通过id删除缴费记录")
    @DeleteMapping("/{seq}")
    @PreAuthorize("@pms.hasPermission('estate_parkbillinginfo_del')")
    public R removeById(@PathVariable Integer seq) {
        return R.ok(parkBillingInfoService.removeById(seq));
    }

    /**
     * 总收入分析分页查询
     *
     * @param vo
     * @return
     */
    @ApiOperation(value = "总收入分析分页查询", notes = "分页查询")
    @GetMapping("/incomePage")
    public R getIncomePage(Page page, ProjectParkBillingTotalSearchConditionVo vo) {
        return R.ok(parkBillingInfoService.getIncome(page, vo));
    }

    /**
     * 收入来源分析分页查询
     *
     * @param vo
     * @return
     */
    @ApiOperation(value = "收入来源分析分页查询", notes = "分页查询")
    @GetMapping("/sourceIncomePage")
    public R getSourceIncomePage(Page page, ProjectParkBillingSourceSearchTotalConditionVo vo) {
        return R.ok(parkBillingInfoService.getSourceIncome(page, vo));
    }

    /**
     * 指定月收入
     *
     * @param date
     * @return
     */
    @ApiOperation(value = "指定月收入", notes = "指定月收入")
    @GetMapping("/monthPayment")
    public R monthPayment(LocalDateTime date) {
        return R.ok(parkBillingInfoService.monthPayment(date));
    }

    /**
     * 指定天收入
     *
     * @param date
     * @return
     */
    @ApiOperation(value = "指定日收入", notes = "指定日收入")
    @GetMapping("/dayPayment")
    public R dayPayment(LocalDateTime date) {
        return R.ok(parkBillingInfoService.dayPayment(date));
    }


}
