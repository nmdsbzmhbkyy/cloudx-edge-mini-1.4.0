package com.aurine.cloudx.open.meta.controller;

import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
import com.aurine.cloudx.open.meta.service.MetaDeviceAttrService;
import com.aurine.cloudx.open.origin.entity.ProjectDeviceAttr;
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
 * 设备拓展属性
 *
 * @author : zouyu
 * @date : 2023-05-04 14:00:55
 */

@RestController
@RequestMapping("/v1/meta/device-attr")
@Api(value = "metaDeviceAttr", tags = {"v1", "设备拓展属性"}, hidden = true)
@Inner
@Slf4j
public class MetaDeviceAttrController {

    @Resource
    private MetaDeviceAttrService metaDeviceAttrService;


    /**
     * 新增设备拓展属性
     *
     * @param model 设备拓展属性
     * @return R 返回新增后的设备拓展属性
     */
    @AutoInject
    @ApiOperation(value = "新增设备拓展属性", notes = "新增设备拓展属性", hidden = true)
    @SysLog("新增设备拓展属性")
    @PostMapping
    public R<ProjectDeviceAttr> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<ProjectDeviceAttr> model) {
        log.info("[MetaDeviceAttrController - save]: 新增设备拓展属性, model={}", JSONConvertUtils.objectToString(model));
        return metaDeviceAttrService.save(model.getData());
    }

    /**
     * 修改设备拓展属性
     *
     * @param model 设备拓展属性
     * @return R 返回修改后的设备拓展属性
     */
    @AutoInject
    @ApiOperation(value = "修改设备拓展属性", notes = "修改设备拓展属性", hidden = true)
    @SysLog("通过id修改设备拓展属性")
    @PutMapping
    public R<ProjectDeviceAttr> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<ProjectDeviceAttr> model) {
        log.info("[MetaDeviceAttrController - update]: 修改设备拓展属性, model={}", JSONConvertUtils.objectToString(model));
        return metaDeviceAttrService.update(model.getData());
    }

    /**
     * 通过id删除设备拓展属性
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除设备拓展属性", notes = "通过id删除设备拓展属性", hidden = true)
    @SysLog("通过id删除设备拓展属性")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[MetaDeviceAttrController - delete]: 通过id删除设备拓展属性, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return metaDeviceAttrService.delete(id);
    }
}
