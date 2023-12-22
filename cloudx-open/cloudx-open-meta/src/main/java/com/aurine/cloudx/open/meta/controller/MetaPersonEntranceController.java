package com.aurine.cloudx.open.meta.controller;

import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.meta.service.MetaPersonEntranceService;
import com.aurine.cloudx.open.origin.entity.ProjectEntranceEvent;
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
 * 人行事件管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/meta/person-entrance")
@Api(value = "metaPersonEntrance", tags = {"v1", "人行事件管理"}, hidden = true)
@Inner
@Slf4j
public class MetaPersonEntranceController {

    @Resource
    private MetaPersonEntranceService metaPersonEntranceService;


    /**
     * 新增人行事件
     *
     * @param model 人行事件
     * @return R 返回新增后的人行事件
     */
    @AutoInject
    @ApiOperation(value = "新增人行事件", notes = "新增人行事件", hidden = true)
    @SysLog("新增人行事件")
    @PostMapping
    public R<ProjectEntranceEvent> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<ProjectEntranceEvent> model) {
        log.info("[MetaPersonEntranceController - save]: 新增人行事件, model={}", JSONConvertUtils.objectToString(model));
        return metaPersonEntranceService.save(model.getData());
    }

    /**
     * 修改人行事件
     *
     * @param model 人行事件
     * @return R 返回修改后的人行事件
     */
    @AutoInject
    @ApiOperation(value = "修改人行事件", notes = "修改人行事件", hidden = true)
    @SysLog("通过id修改人行事件")
    @PutMapping
    public R<ProjectEntranceEvent> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<ProjectEntranceEvent> model) {
        log.info("[MetaPersonEntranceController - update]: 修改人行事件, model={}", JSONConvertUtils.objectToString(model));
        return metaPersonEntranceService.update(model.getData());
    }

    /**
     * 通过id删除人行事件
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除人行事件", notes = "通过id删除人行事件", hidden = true)
    @SysLog("通过id删除人行事件")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[MetaPersonEntranceController - delete]: 通过id删除人行事件, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return metaPersonEntranceService.delete(id);
    }
}
