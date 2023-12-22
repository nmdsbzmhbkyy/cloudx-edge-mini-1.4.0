package com.aurine.cloudx.open.meta.controller;

import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
import com.aurine.cloudx.open.meta.service.MetaEntryExitLaneService;
import com.aurine.cloudx.open.origin.entity.ProjectEntryExitLane;
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
 * 出入口车道管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/meta/entry-exit-lane")
@Api(value = "metaEntryExitLane", tags = {"v1", "出入口车道管理"}, hidden = true)
@Inner
@Slf4j
public class MetaEntryExitLaneController {

    @Resource
    private MetaEntryExitLaneService metaEntryExitLaneService;


    /**
     * 新增出入口车道
     *
     * @param model 出入口车道
     * @return R 返回新增后的出入口车道
     */
    @AutoInject
    @ApiOperation(value = "新增出入口车道", notes = "新增出入口车道", hidden = true)
    @SysLog("新增出入口车道")
    @PostMapping
    public R<ProjectEntryExitLane> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<ProjectEntryExitLane> model) {
        log.info("[MetaEntryExitLaneController - save]: 新增出入口车道, model={}", JSONConvertUtils.objectToString(model));
        return metaEntryExitLaneService.save(model.getData());
    }

    /**
     * 修改出入口车道
     *
     * @param model 出入口车道
     * @return R 返回修改后的出入口车道
     */
    @AutoInject
    @ApiOperation(value = "修改出入口车道", notes = "修改出入口车道", hidden = true)
    @SysLog("通过id修改出入口车道")
    @PutMapping
    public R<ProjectEntryExitLane> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<ProjectEntryExitLane> model) {
        log.info("[MetaEntryExitLaneController - update]: 修改出入口车道, model={}", JSONConvertUtils.objectToString(model));
        return metaEntryExitLaneService.update(model.getData());
    }

    /**
     * 通过id删除出入口车道
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除出入口车道", notes = "通过id删除出入口车道", hidden = true)
    @SysLog("通过id删除出入口车道")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[MetaEntryExitLaneController - delete]: 通过id删除出入口车道, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return metaEntryExitLaneService.delete(id);
    }
}
