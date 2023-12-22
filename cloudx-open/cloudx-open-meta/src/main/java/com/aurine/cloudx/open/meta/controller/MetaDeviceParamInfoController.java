package com.aurine.cloudx.open.meta.controller;

import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
import com.aurine.cloudx.open.meta.service.MetaDeviceParamInfoService;
import com.aurine.cloudx.open.meta.service.MetaPersonLiftRelService;
import com.aurine.cloudx.open.origin.entity.ProjectDeviceParamInfo;
import com.aurine.cloudx.open.origin.entity.ProjectPersonLiftRel;
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
 * 设备参数表
 *
 * @author : zouyu
 * @date : 2022-07-28 10:19:19
 */

@RestController
@RequestMapping("/v1/meta/device-param-info")
@Api(value = "metaDeviceParamInfo", tags = {"v1", "设备参数表"}, hidden = true)
@Inner
@Slf4j
public class MetaDeviceParamInfoController {

    @Resource
    private MetaDeviceParamInfoService metaDeviceParamInfoService;


    /**
     * 新增设备参数
     *
     * @param model 设备参数
     * @return R 返回新增后的设备参数
     */
    @AutoInject
    @ApiOperation(value = "新增设备参数", notes = "新增设备参数", hidden = true)
    @SysLog("新增设备参数")
    @PostMapping
    public R<ProjectDeviceParamInfo> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<ProjectDeviceParamInfo> model) {
        log.info("[MetaDeviceParamInfoController - save]: 新增设备参数, model={}", JSONConvertUtils.objectToString(model));
        return metaDeviceParamInfoService.save(model.getData());
    }

    /**
     * 修改设备参数
     *
     * @param model 设备参数
     * @return R 返回修改后的设备参数
     */
    @AutoInject
    @ApiOperation(value = "修改设备参数", notes = "修改设备参数", hidden = true)
    @SysLog("通过id修改设备参数")
    @PutMapping
    public R<ProjectDeviceParamInfo> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<ProjectDeviceParamInfo> model) {
        log.info("[MetaDeviceParamInfoController - update]: 修改设备参数, model={}", JSONConvertUtils.objectToString(model));
        return metaDeviceParamInfoService.update(model.getData());
    }

    /**
     * 通过id删除设备参数
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除设备参数", notes = "通过id删除设备参数", hidden = true)
    @SysLog("通过id删除设备参数")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[MetaDeviceParamInfoController - delete]: 通过id删除设备参数, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return metaDeviceParamInfoService.delete(id);
    }
}
