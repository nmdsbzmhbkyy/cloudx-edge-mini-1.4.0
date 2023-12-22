package com.aurine.cloudx.open.meta.controller;

import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.meta.service.MetaDeviceSubSystemService;
import com.aurine.cloudx.open.origin.entity.ProjectDeviceSubsystem;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * open平台-设备子系统管理
 *
 * @author : linlx
 * @date : 2022 3 24 9:43
 */

@RestController
@RequestMapping("/v1/meta/project-device-sub-system")
@Api(value = "metaDeviceSubSystem", tags = {"v1", "基础数据相关", "设备子系统管理"}, hidden = true)
@Inner
@Slf4j
public class MetaDeviceSubSystemController {

    @Resource
    private MetaDeviceSubSystemService metaDeviceSubSystemService;


    /**
     * 新增设备子系统
     *
     * @param model 设备子系统
     * @return R 返回新增后的设备子系统
     */
    @AutoInject
    @ApiOperation(value = "新增设备子系统", notes = "新增设备子系统", hidden = true)
    @SysLog("新增设备子系统")
    @PostMapping
    public R<ProjectDeviceSubsystem> save(@RequestBody OpenApiModel<ProjectDeviceSubsystem> model) {
        log.info("[MetaDeviceSubSystemController - save]: 新增设备子系统, model={}", JSONConvertUtils.objectToString(model));
        return metaDeviceSubSystemService.save(model.getData());
    }
}
