package com.aurine.cloudx.open.meta.controller;

import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
import com.aurine.cloudx.open.meta.service.MetaDeviceRelService;
import com.aurine.cloudx.open.origin.entity.ProjectDeviceRel;
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
 * 设备关系管理
 *
 * @author : Qiu
 * @date : 2022 06 13 16:39
 */

@RestController
@RequestMapping("/v1/meta/device-rel")
@Api(value = "metaDeviceRel", tags = {"v1", "设备关系管理"}, hidden = true)
@Inner
@Slf4j
public class MetaDeviceRelController {

    @Resource
    private MetaDeviceRelService metaDeviceRelService;


    /**
     * 新增设备关系
     *
     * @param model 设备关系
     * @return R 返回新增后的设备关系
     */
    @AutoInject
    @ApiOperation(value = "新增设备关系", notes = "新增设备关系", hidden = true)
    @SysLog("新增设备关系")
    @PostMapping
    public R<ProjectDeviceRel> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<ProjectDeviceRel> model) {
        log.info("[MetaDeviceRelController - save]: 新增设备关系, model={}", JSONConvertUtils.objectToString(model));
        return metaDeviceRelService.save(model.getData());
    }

    /**
     * 修改设备关系
     *
     * @param model 设备关系
     * @return R 返回修改后的设备关系
     */
    @AutoInject
    @ApiOperation(value = "修改设备关系", notes = "修改设备关系", hidden = true)
    @SysLog("通过id修改设备关系")
    @PutMapping
    public R<ProjectDeviceRel> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<ProjectDeviceRel> model) {
        log.info("[MetaDeviceRelController - update]: 修改设备关系, model={}", JSONConvertUtils.objectToString(model));
        return metaDeviceRelService.update(model.getData());
    }

    /**
     * 通过id删除设备关系
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除设备关系", notes = "通过id删除设备关系", hidden = true)
    @SysLog("通过id删除设备关系")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[MetaDeviceRelController - delete]: 通过id删除设备关系, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return metaDeviceRelService.delete(id);
    }
}
