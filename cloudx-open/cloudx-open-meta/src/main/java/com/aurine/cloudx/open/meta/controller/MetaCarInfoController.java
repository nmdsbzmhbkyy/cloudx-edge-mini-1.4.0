package com.aurine.cloudx.open.meta.controller;

import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
import com.aurine.cloudx.open.meta.service.MetaCarInfoService;
import com.aurine.cloudx.open.origin.entity.ProjectCarInfo;
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
 * 车辆信息管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/meta/car-info")
@Api(value = "metaCarInfo", tags = {"v1", "车辆信息管理"}, hidden = true)
@Inner
@Slf4j
public class MetaCarInfoController {

    @Resource
    private MetaCarInfoService metaCarInfoService;


    /**
     * 新增车辆信息
     *
     * @param model 车辆信息
     * @return R 返回新增后的车辆信息
     */
    @AutoInject
    @ApiOperation(value = "新增车辆信息", notes = "新增车辆信息", hidden = true)
    @SysLog("新增车辆信息")
    @PostMapping
    public R<ProjectCarInfo> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<ProjectCarInfo> model) {
        log.info("[MetaCarInfoController - save]: 新增车辆信息, model={}", JSONConvertUtils.objectToString(model));
        return metaCarInfoService.save(model.getData());
    }

    /**
     * 修改车辆信息
     *
     * @param model 车辆信息
     * @return R 返回修改后的车辆信息
     */
    @AutoInject
    @ApiOperation(value = "修改车辆信息", notes = "修改车辆信息", hidden = true)
    @SysLog("通过id修改车辆信息")
    @PutMapping
    public R<ProjectCarInfo> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<ProjectCarInfo> model) {
        log.info("[MetaCarInfoController - update]: 修改车辆信息, model={}", JSONConvertUtils.objectToString(model));
        return metaCarInfoService.update(model.getData());
    }

    /**
     * 通过id删除车辆信息
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除车辆信息", notes = "通过id删除车辆信息", hidden = true)
    @SysLog("通过id删除车辆信息")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[MetaCarInfoController - delete]: 通过id删除车辆信息, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return metaCarInfoService.delete(id);
    }
}
