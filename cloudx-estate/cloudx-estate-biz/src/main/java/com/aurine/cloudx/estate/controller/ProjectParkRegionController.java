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
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.estate.constant.ParkRegionIsPublicConstant;
import com.aurine.cloudx.estate.entity.ProjectParkingPlace;
import com.aurine.cloudx.estate.service.ProjectParkingPlaceService;
import com.aurine.cloudx.estate.vo.ProjectParkRegionVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.aurine.cloudx.estate.entity.ProjectParkRegion;
import com.aurine.cloudx.estate.service.ProjectParkRegionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


/**
 * 车位区域
 *
 * @author 王良俊
 * @date 2020-07-07 11:00:29
 */
@RestController
@AllArgsConstructor
@RequestMapping("/projectParkRegion" )
@Api(value = "projectParkRegion", tags = "车位区域管理")
public class ProjectParkRegionController {

    private final ProjectParkRegionService projectParkRegionService;
    private final ProjectParkingPlaceService projectParkingPlaceService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param projectParkRegionVo 车位区域查询条件
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    public R getProjectParkRegionPage(Page page, ProjectParkRegionVo projectParkRegionVo) {
        return R.ok(projectParkRegionService.fetchList(page, projectParkRegionVo));
    }


    /**
     * 通过id查询车位区域
     * @param seq 车位区域ID
     * @return R
     */
    @ApiOperation(value = "通过id查询车位区域", notes = "通过id查询车位区域")
    @GetMapping("/{seq}" )
    public R getById(@PathVariable("seq" ) Integer seq) {
        return R.ok(projectParkRegionService.getById(seq));
    }

    /**
     * 新增车位区域
     * @param projectParkRegion 车位区域對对象
     * @return R
     */
    @ApiOperation(value = "新增车位区域", notes = "新增车位区域")
    @SysLog("新增车位区域" )
    @PostMapping
//    @PreAuthorize("@pms.hasPermission('estate_projectParkRegion_add')" )
    public R save(@RequestBody ProjectParkRegion projectParkRegion) {
        List<ProjectParkRegion> list = projectParkRegionService.list(new QueryWrapper<ProjectParkRegion>().lambda()
                .eq(ProjectParkRegion::getParkId, projectParkRegion.getParkId())
                .eq(ProjectParkRegion::getParkRegionName, projectParkRegion.getParkRegionName()));
        if (CollUtil.isNotEmpty(list)){
            return R.failed("已存在同样的区域名无法添加");
        }
        return R.ok(projectParkRegionService.saveParkRegion(projectParkRegion));
    }

    /**
     * 修改车位区域
     * @param projectParkRegion 车位区域
     * @return R
     */
    @ApiOperation(value = "修改车位区域", notes = "修改车位区域")
    @SysLog("修改车位区域" )
    @PutMapping
//    @PreAuthorize("@pms.hasPermission('estate_projectParkRegion_edit')" )
    public R updateById(@RequestBody ProjectParkRegion projectParkRegion) {
        return projectParkRegionService.updateParkRegion(projectParkRegion);
    }

    /**
     * 通过id删除车位区域
     * @param parkRegionId 车位区域id
     * @return R
     */
//    @PreAuthorize("@pms.hasPermission('estate_projectParkRegion_del')" )
    @ApiOperation(value = "通过id删除车位区域", notes = "通过id删除车位区域")
    @SysLog("通过id删除车位区域" )
    @DeleteMapping("/{parkRegionId}" )
    @Transactional(rollbackFor = Exception.class)
    public R removeById(@PathVariable String parkRegionId) {
        List<ProjectParkingPlace> placeList = projectParkingPlaceService.list(new QueryWrapper<ProjectParkingPlace>()
                .lambda().eq(ProjectParkingPlace::getParkRegionId, parkRegionId));
        if (CollUtil.isNotEmpty(placeList)){
            List<ProjectParkingPlace> list = placeList.stream().filter(projectParkingPlace
                    -> StrUtil.isNotBlank(projectParkingPlace.getPersonId())).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(list)){
                return R.failed("改区域仍有车位被占用无法删除该区域");
            }
        }
        projectParkingPlaceService.remove(new QueryWrapper<ProjectParkingPlace>()
                .lambda().eq(ProjectParkingPlace::getParkRegionId, parkRegionId));
        return R.ok(projectParkRegionService.removeById(parkRegionId));
    }

    /**
     * 通过id查询停车场
     *
     * @param parkRegionId 车位区域id
     * @return R
     */
    @ApiOperation(value = "通过id查询车位区域", notes = "通过id查询车位区域")
    @GetMapping("/getById/{parkRegionId}")
    public R getById(@PathVariable String parkRegionId){
        List<ProjectParkRegion> list = projectParkRegionService.list(new QueryWrapper<ProjectParkRegion>()
                .lambda().eq(ProjectParkRegion::getParkRegionId, parkRegionId));
        if (CollUtil.isNotEmpty(list)){
            return R.ok(list.get(0));
        }else {
            return R.failed("无法获取该车位区域信息");
        }
    }

    /**
     * 检查这个停车场是否已经存在公共停车区域了
     *
     * @param parkId 车场id
     * @return R
     */
    @ApiOperation(value = "检查这个停车场是否已经存在公共停车区域了", notes = "检查这个停车场是否已经存在公共停车区域了")
    @SysLog("通过id删除停车区域")
    @GetMapping("/checkHasPublic/{parkId}/{parkRegionId}")
    public R checkHasPublic(@PathVariable String parkId, @PathVariable String parkRegionId) {
        return R.ok(projectParkRegionService.checkHasPublic(parkId, parkRegionId));
    }

    /**
     * 检查这个停车场是否已经存在公共停车区域了
     *
     * @param parkId 车场id
     * @return R
     */
    @ApiOperation(value = "检查这个停车场是否已经存在公共停车区域了", notes = "检查这个停车场是否已经存在公共停车区域了")
    @SysLog("通过id删除停车区域")
    @GetMapping("/checkHasPublic/{parkId}")
    public R checkHasPublic(@PathVariable String parkId) {
        return R.ok(projectParkRegionService.checkHasPublic(parkId, ""));
    }

    /**
     * 获取车位区域列表
     *
     * @return R
     */
    @ApiOperation(value = "获取车位区域列表", notes = "获取车位区域列表")
    @SysLog("通过车场id获取到车位区域列表")
    @GetMapping("/list/{parkId}")
    public R getList(@PathVariable String parkId) {
        boolean doFilterByParkId = false;
        if (StrUtil.isNotBlank(parkId) && !("undefined".equals(parkId))){
            doFilterByParkId = true;
        }
        /*list(new QueryWrapper<ProjectParkRegion>()
                .lambda().eq(doFilterByParkId, PbaseParkingPlacerojectParkRegion::getParkId, parkId)
                .notIn(ProjectParkRegion::getIsPublic, ParkRegionIsPublicConstant.PUBLIC))*/
        return R.ok(projectParkRegionService.listByParkId(parkId));
    }

    /**
     * <p>
     *  获取车位区域列表
     * </p>
     *
     * @param projectParkingPlace 车位对象
     * @return
     * @throws
    */
    @ApiOperation(value = "获取车位区域列表", notes = "获取车位区域列表")
    @SysLog("获取车位区域列表")
    @GetMapping("/listPersonAttrParkingRegionByRelType")
    public R getList(String personId, String relType, String parkId, String placeId) {
        return R.ok(projectParkRegionService.listPersonAttrParkingRegionByRelType(parkId
                , personId
                , relType
                , placeId));
    }
}
