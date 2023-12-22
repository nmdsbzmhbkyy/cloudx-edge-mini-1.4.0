package com.aurine.cloudx.open.meta.controller;

import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.meta.service.MetaPersonDeviceRelService;
import com.aurine.cloudx.open.origin.entity.ProjectPersonDevice;
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
 * 人员设备权限关系管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/meta/person-device-rel")
@Api(value = "metaPersonDeviceRel", tags = {"v1", "通行方案相关", "人员设备权限关系管理"}, hidden = true)
@Inner
@Slf4j
public class MetaPersonDeviceRelController {

    @Resource
    private MetaPersonDeviceRelService metaPersonDeviceRelService;


    /**
     * 新增人员设备权限关系
     *
     * @param model 人员设备权限关系
     * @return R 返回新增后的人员设备权限关系
     */
    @AutoInject
    @ApiOperation(value = "新增人员设备权限关系", notes = "新增人员设备权限关系", hidden = true)
    @SysLog("新增人员设备权限关系")
    @PostMapping
    public R<ProjectPersonDevice> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<ProjectPersonDevice> model) {
        log.info("[MetaPersonDeviceRelController - save]: 新增人员设备权限关系, model={}", JSONConvertUtils.objectToString(model));
        return metaPersonDeviceRelService.save(model.getData());
    }

    /**
     * 修改人员设备权限关系
     *
     * @param model 人员设备权限关系
     * @return R 返回修改后的人员设备权限关系
     */
    @AutoInject
    @ApiOperation(value = "修改人员设备权限关系", notes = "修改人员设备权限关系", hidden = true)
    @SysLog("通过id修改人员设备权限关系")
    @PutMapping
    public R<ProjectPersonDevice> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<ProjectPersonDevice> model) {
        log.info("[MetaPersonDeviceRelController - update]: 修改人员设备权限关系, model={}", JSONConvertUtils.objectToString(model));
        return metaPersonDeviceRelService.update(model.getData());
    }

    /**
     * 通过id删除人员设备权限关系
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除人员设备权限关系", notes = "通过id删除人员设备权限关系", hidden = true)
    @SysLog("通过id删除人员设备权限关系")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[MetaPersonDeviceRelController - delete]: 通过id删除人员设备权限关系, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return metaPersonDeviceRelService.delete(id);
    }
}
