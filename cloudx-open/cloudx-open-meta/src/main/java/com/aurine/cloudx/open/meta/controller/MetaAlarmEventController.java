package com.aurine.cloudx.open.meta.controller;

import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
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
@RequestMapping("/v1/meta/alarm-event")
@Api(value = "metaAlarmEvent", tags = {"v1", "报警事件管理"}, hidden = true)
@Inner
@Slf4j
public class MetaAlarmEventController {

    @Resource
    private MetaAlarmEventService metaAlarmEventService;


    /**
     * 新增报警事件
     *
     * @param model 报警事件
     * @return R 返回新增后的报警事件
     */
    @AutoInject
    @ApiOperation(value = "新增报警事件", notes = "新增报警事件", hidden = true)
    @SysLog("新增报警事件")
    @PostMapping
    public R<ProjectEntranceAlarmEvent> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<ProjectEntranceAlarmEvent> model) {
        log.info("[MetaAlarmEventController - save]: 新增报警事件, model={}", JSONConvertUtils.objectToString(model));
        return metaAlarmEventService.save(model.getData());
    }

    /**
     * 修改报警事件
     *
     * @param model 报警事件
     * @return R 返回修改后的报警事件
     */
    @AutoInject
    @ApiOperation(value = "修改报警事件", notes = "修改报警事件", hidden = true)
    @SysLog("通过id修改报警事件")
    @PutMapping
    public R<ProjectEntranceAlarmEvent> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<ProjectEntranceAlarmEvent> model) {
        log.info("[MetaAlarmEventController - update]: 修改报警事件, model={}", JSONConvertUtils.objectToString(model));
        return metaAlarmEventService.update(model.getData());
    }

    /**
     * 通过id删除报警事件
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除报警事件", notes = "通过id删除报警事件", hidden = true)
    @SysLog("通过id删除报警事件")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[MetaAlarmEventController - delete]: 通过id删除报警事件, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return metaAlarmEventService.delete(id);
    }
}
