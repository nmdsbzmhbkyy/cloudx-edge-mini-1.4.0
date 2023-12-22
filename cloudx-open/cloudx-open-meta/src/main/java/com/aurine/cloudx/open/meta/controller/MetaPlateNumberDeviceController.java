package com.aurine.cloudx.open.meta.controller;

import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
import com.aurine.cloudx.open.meta.service.MetaPlateNumberDeviceService;
import com.aurine.cloudx.open.origin.entity.ProjectPlateNumberDevice;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 设备车牌号下发情况表管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/meta/plate-number-device")
@Api(value = "metaPlateNumberDevice", tags = {"v1", "设备车牌号下发情况表管理"}, hidden = true)
@Inner
@Slf4j
public class MetaPlateNumberDeviceController {

    @Resource
    private MetaPlateNumberDeviceService metaPlateNumberDeviceService;


    /**
     * 新增设备车牌号下发情况
     *
     * @param model 设备车牌号下发情况表
     * @return R 返回新增后的设备车牌号下发情况表
     */
    @AutoInject
    @ApiOperation(value = "新增设备车牌号下发情况", notes = "新增设备车牌号下发情况", hidden = true)
    @SysLog("新增设备车牌号下发情况")
    @PostMapping
    public R<ProjectPlateNumberDevice> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<ProjectPlateNumberDevice> model) {
        log.info("[MetaPlateNumberDeviceController - save]: 新增设备车牌号下发情况, model={}", JSONConvertUtils.objectToString(model));
        return metaPlateNumberDeviceService.save(model.getData());
    }

    /**
     * 修改设备车牌号下发情况表
     *
     * @param model 设备车牌号下发情况表
     * @return R 返回修改后的设备车牌号下发情况表
     */
    @AutoInject
    @ApiOperation(value = "修改设备车牌号下发情况表", notes = "修改设备车牌号下发情况表", hidden = true)
    @SysLog("通过id修改设备车牌号下发情况表")
    @PutMapping
    public R<ProjectPlateNumberDevice> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<ProjectPlateNumberDevice> model) {
        log.info("[MetaPlateNumberDeviceController - update]: 修改设备车牌号下发情况表, model={}", JSONConvertUtils.objectToString(model));
        return metaPlateNumberDeviceService.update(model.getData());
    }

    /**
     * 通过id删除设备车牌号下发情况表
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除设备车牌号下发情况表", notes = "通过id删除设备车牌号下发情况表", hidden = true)
    @SysLog("通过id删除设备车牌号下发情况表")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[MetaPlateNumberDeviceController - delete]: 通过id删除设备车牌号下发情况表, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return metaPlateNumberDeviceService.delete(id);
    }
}
