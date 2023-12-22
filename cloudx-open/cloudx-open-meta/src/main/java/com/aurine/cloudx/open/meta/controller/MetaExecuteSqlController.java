package com.aurine.cloudx.open.meta.controller;

import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
import com.aurine.cloudx.open.meta.service.ExecuteSqlService;
import com.aurine.cloudx.open.meta.service.MetaAlarmEventService;
import com.aurine.cloudx.open.origin.entity.ProjectEntranceAlarmEvent;
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
 * 报警事件管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/meta/executeSql")
@Api(value = "executeSql", tags = {"v1", "sql脚本处理"}, hidden = true)
@Inner
@Slf4j
public class MetaExecuteSqlController {


    @Resource
    private ExecuteSqlService executeSqlService;

    /**
     * 执行新增sql
     * @param model
     * @return
     */
    @AutoInject
    @ApiOperation(value = "执行新增sql", notes = "执行新增sql", hidden = true)
    @SysLog("执行新增sql")
    @PostMapping
    public R<Boolean> save( @RequestBody OpenApiModel model) {
        log.info("[executeSql - save]: 执行新增sql, data={}", JSONConvertUtils.objectToString(model));
        return executeSqlService.save(model);
    }

    /**
     * 执行更新sql
     * @param model
     * @return
     */
    @AutoInject
    @ApiOperation(value = "执行更新sql", notes = "执行更新sql", hidden = true)
    @SysLog("执行更新sql")
    @PutMapping
    public R<Boolean> update( @RequestBody OpenApiModel model) {
        log.info("[executeSql - update]: 执行更新sql, data={}", JSONConvertUtils.objectToString(model));
        return executeSqlService.update(model);
    }

    /**
     * 执行删除sql
     * @param model
     * @return
     */
    @ApiOperation(value = "执行删除sql", notes = "执行删除sql", hidden = true)
    @SysLog("执行删除sql")
    @DeleteMapping
    @AutoInject
    public R<Boolean> delete(@RequestBody OpenApiModel model) {
        log.info("[executeSql - delete]: 执行删除sql, data={}", JSONConvertUtils.objectToString(model));
        return executeSqlService.delete(model);
    }
}
