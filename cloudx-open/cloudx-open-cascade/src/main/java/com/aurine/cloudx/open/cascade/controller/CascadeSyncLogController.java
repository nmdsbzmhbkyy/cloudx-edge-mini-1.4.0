package com.aurine.cloudx.open.cascade.controller;

import com.aurine.cloudx.open.cascade.service.CascadeSyncLogService;
import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.origin.entity.EdgeSyncLog;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 级联入云同步日志
 *
 * @author : linlx
 * @date : 2022 3 24 9:43
 */

@RestController
@RequestMapping("v1/cascade/sync-log")
@Api(value = "cascadeSyncLog", tags = {"v1", "级联入云相关", "级联入云同步日志"})
@Inner
@Slf4j
public class CascadeSyncLogController {

    @Resource
    private CascadeSyncLogService cascadeSyncLogService;


    /**
     * 新增级联入云同步日志
     *
     * @param model 级联入云同步日志
     * @return R 返回新增后的级联入云同步日志
     */
    @AutoInject
    @ApiOperation(value = "新增级联入云同步日志", notes = "新增级联入云同步日志")
    @SysLog("新增级联入云同步日志")
    @PostMapping
    public R<EdgeSyncLog> save(@RequestBody OpenApiModel<EdgeSyncLog> model) {
        log.info("[CascadeSyncLogController - save]: 新增级联入云同步日志, model={}", JSONConvertUtils.objectToString(model));
        return cascadeSyncLogService.save(model.getData());
    }

    /**
     * 修改级联入云同步日志
     *
     * @param model 级联入云同步日志
     * @return R 返回修改后的级联入云同步日志
     */
    @AutoInject
    @ApiOperation(value = "修改级联入云同步日志", notes = "修改级联入云同步日志")
    @SysLog("修改级联入云同步日志")
    @PutMapping
    public R<EdgeSyncLog> update(@RequestBody OpenApiModel<EdgeSyncLog> model) {
        log.info("[CascadeSyncLogController - update]: 修改级联入云同步日志, model={}", JSONConvertUtils.objectToString(model));
        return cascadeSyncLogService.update(model.getData());
    }
}
