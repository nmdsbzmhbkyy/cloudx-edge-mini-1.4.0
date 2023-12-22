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
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/parkEntranceHis" )
@Api(value = "parkEntranceHis", tags = "车行记录")
public class ProjectParkEntranceHisController {

    private final ProjectParkEntranceHisService parkEntranceHisService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param projectParkEntranceHisVo 车场管理
     * @return
     */
    @ApiOperation(value = "查询车辆的出入记录列表", notes = "查询车辆的出入记录列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parkId", value = "车场Id", required = false, paramType = "param"),
            @ApiImplicitParam(name = "plateNumber", value = "车牌号", required = false, paramType = "param"),
            @ApiImplicitParam(name = "status", value = "出入状态(1 入场 2 出场)", required = false, paramType = "param")
    })
    @GetMapping("/page")
    public R<IPage<ProjectParkEntranceHisVo>> getParkEntranceHisPage(Page page, ProjectParkEntranceHisVo projectParkEntranceHisVo) {
        return R.ok(parkEntranceHisService.page(page, projectParkEntranceHisVo));
    }
}
