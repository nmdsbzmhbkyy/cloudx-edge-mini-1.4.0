package com.aurine.cloudx.open.meta.controller;

import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.meta.service.MetaRightDeviceRelService;
import com.aurine.cloudx.open.origin.entity.ProjectRightDevice;
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
 * 权限设备关系管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/meta/right-device-rel")
@Api(value = "metaRightDeviceRel", tags = {"v1", "权限设备关系管理"}, hidden = true)
@Inner
@Slf4j
public class MetaRightDeviceRelController {

    @Resource
    private MetaRightDeviceRelService metaRightDeviceRelService;


    /**
     * 新增权限设备关系
     *
     * @param model 权限设备关系
     * @return R 返回新增后的权限设备关系
     */
    @AutoInject
    @ApiOperation(value = "新增权限设备关系", notes = "新增权限设备关系", hidden = true)
    @SysLog("新增权限设备关系")
    @PostMapping
    public R<ProjectRightDevice> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<ProjectRightDevice> model) {
        log.info("[MetaRightDeviceController - save]: 新增权限设备关系, model={}", JSONConvertUtils.objectToString(model));
        return metaRightDeviceRelService.save(model.getData());
    }

    /**
     * 修改权限设备关系
     *
     * @param model 权限设备关系
     * @return R 返回修改后的权限设备关系
     */
    @AutoInject
    @ApiOperation(value = "修改权限设备关系", notes = "修改权限设备关系", hidden = true)
    @SysLog("通过id修改权限设备关系")
    @PutMapping
    public R<ProjectRightDevice> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<ProjectRightDevice> model) {
        log.info("[MetaRightDeviceController - update]: 修改权限设备关系, model={}", JSONConvertUtils.objectToString(model));
        return metaRightDeviceRelService.update(model.getData());
    }

    /**
     * 通过id删除权限设备关系
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除权限设备关系", notes = "通过id删除权限设备关系", hidden = true)
    @SysLog("通过id删除权限设备关系")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[MetaRightDeviceController - delete]: 通过id删除权限设备关系, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return metaRightDeviceRelService.delete(id);
    }
}
