package com.aurine.cloudx.open.biz.controller;

import com.aurine.cloudx.open.biz.service.OpenDeviceInfoService;
import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.entity.model.OpenApiPageModel;
import com.aurine.cloudx.open.common.entity.vo.DeviceInfoVo;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
@RequestMapping("/v1/open/device-info")
@Api(value = "openDeviceInfo", tags = {"v1", "设备信息管理"})
@Inner
@Slf4j
public class OpenDeviceInfoController {

    @Resource
    private OpenDeviceInfoService openDeviceInfoService;


    /**
     * 通过id查询设备信息
     *
     * @param header 请求头信息
     * @param id     要查询的主键id
     * @return R 返回设备信息
     */
    @ApiOperation(value = "通过id查询设备信息", notes = "通过id查询设备信息")
    @SysLog("通过id查询设备信息")
    @GetMapping("/{id}")
    public R<DeviceInfoVo> getById(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[OpenDeviceInfoController - getById]: 通过id查询设备信息, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return openDeviceInfoService.getById(id);
    }

    /**
     * 分页查询设备信息
     *
     * @param pageModel 分页查询条件
     * @return R 返回设备信息分页数据
     */
    @AutoInject
    @ApiOperation(value = "分页查询设备信息", notes = "分页查询设备信息")
    @SysLog("分页查询设备信息")
    @PostMapping("/page")
    public R<Page<DeviceInfoVo>> page(@Validated @RequestBody OpenApiPageModel<DeviceInfoVo> pageModel) {
        log.info("[OpenDeviceInfoController - page]: 分页查询设备信息, page={}, query={}", JSONConvertUtils.objectToString(pageModel.getPage()), JSONConvertUtils.objectToString(pageModel.getData()));
        return openDeviceInfoService.page(pageModel.getPage(), pageModel.getData());
    }

    /**
     * 新增设备信息
     *
     * @param model 设备信息
     * @return R 返回新增后的设备信息
     */
    @AutoInject
    @ApiOperation(value = "新增设备信息", notes = "新增设备信息")
    @SysLog("新增设备信息")
    @PostMapping
    public R<DeviceInfoVo> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<DeviceInfoVo> model) {
        log.info("[OpenDeviceInfoController - save]: 新增设备信息, model={}", JSONConvertUtils.objectToString(model));
        return openDeviceInfoService.save(model.getData());
    }

    /**
     * 修改设备信息
     *
     * @param model 设备信息
     * @return R 返回修改后的设备信息
     */
    @AutoInject
    @ApiOperation(value = "修改设备信息", notes = "修改设备信息")
    @SysLog("通过id修改设备信息")
    @PutMapping
    public R<DeviceInfoVo> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<DeviceInfoVo> model) {
        log.info("[OpenDeviceInfoController - update]: 修改设备信息, model={}", JSONConvertUtils.objectToString(model));
        return openDeviceInfoService.update(model.getData());
    }

    /**
     * 通过id删除设备信息
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除设备信息", notes = "通过id删除设备信息")
    @SysLog("通过id删除设备信息")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[OpenDeviceInfoController - delete]: 通过id删除设备信息, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return openDeviceInfoService.delete(id);
    }
}
