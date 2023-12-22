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

import com.aurine.cloudx.estate.entity.ProjectPerimeterAlarmRecord;
import com.aurine.cloudx.estate.service.ProjectPerimeterAlarmRecordService;
import com.aurine.cloudx.estate.vo.ProjectPerimeterAlarmRecordVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 周界报警布防/撤防记录表
 *
 * @author pigx code generator
 * @date 2022-07-04 16:10:18
 */
@RestController
@AllArgsConstructor
@RequestMapping("/projectperimeteralarmrecord" )
@Api(value = "projectperimeteralarmrecord", tags = "周界报警布防/撤防记录表管理")
public class ProjectPerimeterAlarmRecordController {

    private final ProjectPerimeterAlarmRecordService projectPerimeterAlarmRecordService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param projectPerimeterAlarmRecordVo 周界报警布防/撤防记录Vo
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    public R getProjectPerimeterAlarmRecordPage(Page page, ProjectPerimeterAlarmRecordVo projectPerimeterAlarmRecordVo) {
        return R.ok(projectPerimeterAlarmRecordService.findPage(page, projectPerimeterAlarmRecordVo));
    }
}
