package com.aurine.cloudx.open.meta.controller;

import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.meta.service.MetaDeviceInfoService;
import com.aurine.cloudx.open.origin.entity.ProjectDeviceInfo;
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
 * 设备信息管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/meta/device-info")
@Api(value = "metaDeviceInfo", tags = {"v1", "设备信息管理"}, hidden = true)
@Inner
@Slf4j
public class MetaDeviceInfoController {

    @Resource
    private MetaDeviceInfoService metaDeviceInfoService;


    /**
     * 新增设备信息
     *
     * @param model 设备信息
     * @return R 返回新增后的设备信息
     */
    @AutoInject
    @ApiOperation(value = "新增设备信息", notes = "新增设备信息", hidden = true)
    @SysLog("新增设备信息")
    @PostMapping
    public R<ProjectDeviceInfo> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<ProjectDeviceInfo> model) {
        log.info("[MetaDeviceInfoController - save]: 新增设备信息, model={}", JSONConvertUtils.objectToString(model));
        return metaDeviceInfoService.save(model.getData());
    }

    /**
     * 修改设备信息
     *
     * @param model 设备信息
     * @return R 返回修改后的设备信息
     */
    @AutoInject
    @ApiOperation(value = "修改设备信息", notes = "修改设备信息", hidden = true)
    @SysLog("通过id修改设备信息")
    @PutMapping
    public R<ProjectDeviceInfo> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<ProjectDeviceInfo> model) {
        log.info("[MetaDeviceInfoController - update]: 修改设备信息, model={}", JSONConvertUtils.objectToString(model));
        return metaDeviceInfoService.update(model.getData());
    }

    /**
     * 通过id删除设备信息
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除设备信息", notes = "通过id删除设备信息", hidden = true)
    @SysLog("通过id删除设备信息")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[MetaDeviceInfoController - delete]: 通过id删除设备信息, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return metaDeviceInfoService.delete(id);
    }
}
