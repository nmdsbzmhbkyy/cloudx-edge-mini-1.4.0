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
import com.aurine.cloudx.estate.entity.ProjectCarInfo;
import com.aurine.cloudx.estate.entity.ProjectCarPreRegister;
import com.aurine.cloudx.estate.entity.ProjectParCarRegister;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.vo.ProjectCarInfoVo;
import com.aurine.cloudx.estate.vo.ProjectParCarRegisterSeachConditionVo;
import com.aurine.cloudx.estate.vo.ProjectParCarRegisterVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


/**
 * 车辆登记
 *
 * @author 王伟
 * @date 2020-07-08 14:10:32
 */
@RestController
@RequestMapping("/projectParCarRegister")
@Api(value = "projectParCarRegister", tags = "车辆登记管理")
public class ProjectParCarRegisterController {

    @Autowired
    private ProjectParCarRegisterService projectParCarRegisterService;
    @Autowired
    private ProjectCarInfoService projectCarInfoService;
    @Autowired
    private ProjectHousePersonRelService projectHousePersonRelService;
    @Autowired
    private ProjectParkingPlaceService projectParkingPlaceService;
    @Autowired
    private ProjectInspectTaskService projectInspectTaskService;
    @Autowired
    private ProjectPersonInfoService projectPersonInfoService;

    @Resource
    private ProjectCarPreRegisterService projectCarPreRegisterService;


    /**
     * 分页查询
     *
     * @param page            分页对象
     * @param searchCondition 车辆登记
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R getProjectParCarRegisterPage(Page page, ProjectParCarRegisterSeachConditionVo searchCondition) {
        return R.ok(projectParCarRegisterService.pageCarRegister(page, searchCondition));
    }

    @ApiOperation(value = "导出", notes = "导出")
    @PostMapping("/registerExportExcel")
    public R testExport(Page page, ProjectParCarRegisterSeachConditionVo searchCondition, HttpServletResponse httpServletResponse) {
        projectParCarRegisterService.pageCarRegisterExport(page, searchCondition, httpServletResponse);
        return R.ok();
    }


    /**
     * 通过id查询车辆登记
     *
     * @param registerId id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{registerId}")
    public R getById(@PathVariable("registerId") String registerId) {
//        projectInspectTaskService.initTask();
        return R.ok(projectParCarRegisterService.getVo(registerId));
    }

    /**
     * 通过车牌号查询车辆以及所有人的信息
     *
     * @param plateNumber 通过车牌号查询
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过车牌号查询")
    @GetMapping("/getCarVoByPlateNumber/{plateNumber}")
    public R getCarVoByPlateNumber(@PathVariable("plateNumber") String plateNumber) {
        List<ProjectParCarRegister> parCarRegisterList = projectParCarRegisterService.list(
                new QueryWrapper<ProjectParCarRegister>().lambda().eq(ProjectParCarRegister::getPlateNumber, plateNumber));
        if (CollUtil.isNotEmpty(parCarRegisterList)) {
            throw new RuntimeException("该车牌号已经登记过了，无法再次登记。");
        }
        ProjectCarInfoVo voByPlateNumber = projectCarInfoService.getVoByPlateNumber(plateNumber);

        return R.ok(voByPlateNumber);
    }

    /**
     * 新增车辆登记
     *
     * @param projectParCarRegister 车辆登记
     * @return R
     */
    @ApiOperation(value = "新增车辆登记", notes = "新增车辆登记")
    @SysLog("新增车辆登记")
    @PostMapping
//    @PreAuthorize("@pms.hasPermission('estate_carregister_add')")
    public R register(@RequestBody ProjectParCarRegisterVo projectParCarRegister) {
        projectParCarRegister.setSource("register");
        return R.ok(projectParCarRegisterService.saveCarRegister(projectParCarRegister));
    }

    /**
     * <p>
     * 导入Excel文件
     * </p>
     *
     * @param file Excel文件对象
     * @author: 王良俊
     */
    @ApiOperation(value = "导入Excel文件", notes = "导入Excel文件")
    @SysLog("导入Excel文件")
    @PostMapping("/importExcel/{type}")
    public R importExcel(@PathVariable("type") String type, @RequestParam("file") MultipartFile file) {
        return projectParCarRegisterService.importExcel(file, type);
    }


    /**
     * <p>
     * 获取错误Excel
     * </p>
     *
     * @param name 文件名
     * @author: 许亮亮
     */
    @GetMapping("/errorExcel/{name}")
    @ApiModelProperty(value = "获取导入失败列表", notes = "获取导入失败列表")
    @Inner(false)
    public void errorExcel(@PathVariable("name") String name, HttpServletResponse httpServletResponse) throws IOException {
        projectParCarRegisterService.errorExcel(name, httpServletResponse);
    }

    /**
     * <p>
     * 获取导入模板
     * </p>
     *
     * @author: 许亮亮
     */
    @GetMapping("/modelExcel/{type}")
    @ApiModelProperty(value = "获取导入模板", notes = "获取导入模板")
    @Inner(false)
    public void modelExcel(@PathVariable("type") String type, HttpServletResponse httpServletResponse) throws IOException {
        projectParCarRegisterService.modelExcel(type, httpServletResponse);
    }

    /**
     * 修改车辆登记
     *
     * @param projectParCarRegister 车辆登记
     * @return R
     */
    @ApiOperation(value = "修改车辆登记", notes = "修改车辆登记")
    @SysLog("修改车辆登记")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('estate_carregister_edit')")
    public R updateById(@RequestBody ProjectParCarRegisterVo projectParCarRegister) {
        return R.ok(projectParCarRegisterService.updateCarRegister(projectParCarRegister));
    }

    /**
     * 通过id删除车辆登记
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除车辆登记", notes = "通过id删除车辆登记")
    @SysLog("通过id删除车辆登记")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('estate_carregister_del')")
    public R removeById(@PathVariable String id) {
        return R.ok(projectParCarRegisterService.removeById(id));
    }

    /**
     * 注销车辆登记
     *
     * @param registerId id
     * @return R
     */
    @ApiOperation(value = "通过id删除车辆登记", notes = "通过id删除车辆登记")
    @SysLog("通过id删除车辆登记")
    @GetMapping("/cancelCarRegister/{registerId}")
//    @PreAuthorize("@pms.hasPermission('estate_carregister_del')")
    public R cancelCarRegister(@PathVariable String registerId) {
        ProjectParCarRegister parCarRegister = projectParCarRegisterService.getById(registerId);
        ProjectCarInfo carInfo = null;
        if (parCarRegister != null) {
            carInfo = projectCarInfoService.getOne(new QueryWrapper<ProjectCarInfo>().lambda().eq(ProjectCarInfo::getPlateNumber, parCarRegister.getPlateNumber()));
        }
        boolean result = projectParCarRegisterService.cancelCarRegister(registerId);
        if (carInfo != null) {
            projectPersonInfoService.checkPersonAssets(carInfo.getPersonId());
        }
        projectCarPreRegisterService.remove(new QueryWrapper<ProjectCarPreRegister>().lambda()
                .eq(ProjectCarPreRegister::getPlateNumber, parCarRegister.getPlateNumber()));
        return R.ok(result);
    }


    /**
     * 注销车辆登记
     *
     * @param cancelRegisterIdList id
     * @return R
     */
    @ApiOperation(value = "通过id删除车辆登记", notes = "通过id删除车辆登记")
    @SysLog("通过id删除车辆登记")
    @PostMapping("/cancelCarRegisterList")
    @PreAuthorize("@pms.hasPermission('estate_carregister_del')")
    public R cancelCarRegisterList(@RequestBody List<String> cancelRegisterIdList) {
        return R.ok(this.projectParCarRegisterService.cancelCarRegister(cancelRegisterIdList));
    }

    /**
     * 更新租赁时间
     *
     * @param vo 登记vo对象
     * @return R
     */
    @ApiOperation(value = "更新租赁时间", notes = "更新租赁时间")
    @SysLog("更新租赁时间")
    @PostMapping("/delay")
    public R delay(@RequestBody ProjectParCarRegisterVo vo) {
        return R.ok(projectParCarRegisterService.delay(vo));
    }

    /**
     * 判断车位是否已被登记
     *
     * @param placeId 车位ID
     * @return R
     */
    @ApiOperation(value = "判断车位是否已被登记", notes = "判断车位是否已被登记")
    @SysLog("判断车位是否已被登记")
    @GetMapping("/checkHasRegister/{placeId}")
    public R checkHasRegister(@PathVariable("placeId") String placeId) {
        return R.ok(projectParCarRegisterService.checkHasRegister(placeId));
    }


    /**
     * 判断车牌号是否被登记过了
     *
     * @param plateNumber 车牌号
     * @return R
     */
    @ApiOperation(value = "判断车牌号是否被登记过了", notes = "判断车牌号是否被登记过了")
    @SysLog("判断车牌号是否被登记过了")
    @GetMapping("/checkIsRegister/{plateNumber}")
    public R checkIsRegister(@PathVariable("plateNumber") String plateNumber) {

        return R.ok(projectParCarRegisterService.count(new QueryWrapper<ProjectParCarRegister>().lambda()
                .eq(ProjectParCarRegister::getPlateNumber, plateNumber)) != 0);
    }

    /**
     * 判断当前项目是否已开启一位多车
     *
     * @return R
     */
    @ApiOperation(value = "判断当前项目是否已开启一位多车", notes = "判断当前项目是否已开启一位多车")
    @SysLog("判断当前项目是否已开启一位多车")
    @GetMapping("/isAlreadyAMultiCar")
    public R isAlreadyAMultiCar() {
        return R.ok(projectParCarRegisterService.isAlreadyAMultiCar());
    }

    /**
     * 返回同一车位上第一辆车的有效期和收费方式等
     *
     * @return R
     */
    @ApiOperation(value = "返回同一车位上第一辆车的有效期和收费方式等", notes = "返回同一车位上第一辆车的有效期和收费方式等")
    @SysLog("返回同一车位上第一辆车的有效期和收费方式")
    @PostMapping("/getValidityByPlaceId")
    public R getValidityByPlaceId(ProjectParCarRegisterVo projectParCarRegister) {

        return R.ok(projectParCarRegisterService.getValidityByPlaceId(projectParCarRegister));
    }

    /**
     * 返回已登记车位和已到期车辆
     *
     * @return R
     */
    @ApiOperation(value = "返回已登记车位和已到期车辆", notes = "返回已登记车位和已到期车辆")
    @SysLog("返回已登记车位和已到期车辆")
    @GetMapping("/getRegisterAndExpire")
    public R getRegisterAndExpire() {

        return R.ok(projectParCarRegisterService.getRegisterAndExpire());
    }
}
