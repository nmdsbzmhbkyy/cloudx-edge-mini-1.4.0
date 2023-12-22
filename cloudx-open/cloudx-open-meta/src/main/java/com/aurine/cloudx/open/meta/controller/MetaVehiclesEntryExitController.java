package com.aurine.cloudx.open.meta.controller;

import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
import com.aurine.cloudx.open.meta.service.MetaVehiclesEntryExitService;
import com.aurine.cloudx.open.origin.entity.ProjectVehiclesEntryExit;
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
 * 车辆出入口信息管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/meta/vehicles-entry-exit")
@Api(value = "metaVehiclesEntryExit", tags = {"v1", "车辆出入口信息管理"}, hidden = true)
@Inner
@Slf4j
public class MetaVehiclesEntryExitController {

    @Resource
    private MetaVehiclesEntryExitService metaVehiclesEntryExitService;


    /**
     * 新增车辆出入口信息
     *
     * @param model 车辆出入口信息
     * @return R 返回新增后的车辆出入口信息
     */
    @AutoInject
    @ApiOperation(value = "新增车辆出入口信息", notes = "新增车辆出入口信息", hidden = true)
    @SysLog("新增车辆出入口信息")
    @PostMapping
    public R<ProjectVehiclesEntryExit> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<ProjectVehiclesEntryExit> model) {
        log.info("[MetaVehiclesEntryExitController - save]: 新增车辆出入口信息, model={}", JSONConvertUtils.objectToString(model));
        return metaVehiclesEntryExitService.save(model.getData());
    }

    /**
     * 修改车辆出入口信息
     *
     * @param model 车辆出入口信息
     * @return R 返回修改后的车辆出入口信息
     */
    @AutoInject
    @ApiOperation(value = "修改车辆出入口信息", notes = "修改车辆出入口信息", hidden = true)
    @SysLog("通过id修改车辆出入口信息")
    @PutMapping
    public R<ProjectVehiclesEntryExit> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<ProjectVehiclesEntryExit> model) {
        log.info("[MetaVehiclesEntryExitController - update]: 修改车辆出入口信息, model={}", JSONConvertUtils.objectToString(model));
        return metaVehiclesEntryExitService.update(model.getData());
    }

    /**
     * 通过id删除车辆出入口信息
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除车辆出入口信息", notes = "通过id删除车辆出入口信息", hidden = true)
    @SysLog("通过id删除车辆出入口信息")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[MetaVehiclesEntryExitController - delete]: 通过id删除车辆出入口信息, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return metaVehiclesEntryExitService.delete(id);
    }
}
