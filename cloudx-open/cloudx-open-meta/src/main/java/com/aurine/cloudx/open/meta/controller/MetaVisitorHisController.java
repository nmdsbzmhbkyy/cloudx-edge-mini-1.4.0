package com.aurine.cloudx.open.meta.controller;

import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.meta.service.MetaVisitorHistService;
import com.aurine.cloudx.open.origin.entity.ProjectVisitorHis;
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
 * 来访记录管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/meta/visitor-his")
@Api(value = "metaVisitorHis", tags = {"v1", "来访记录管理"}, hidden = true)
@Inner
@Slf4j
public class MetaVisitorHisController {

    @Resource
    private MetaVisitorHistService metaVisitorHistService;


    /**
     * 新增来访记录
     *
     * @param model 来访记录
     * @return R 返回新增后的来访记录
     */
    @AutoInject
    @ApiOperation(value = "新增来访记录", notes = "新增来访记录", hidden = true)
    @SysLog("新增来访记录")
    @PostMapping
    public R<ProjectVisitorHis> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<ProjectVisitorHis> model) {
        log.info("[MetaVisitorHisController - save]: 新增来访记录, model={}", JSONConvertUtils.objectToString(model));
        return metaVisitorHistService.save(model.getData());
    }

    /**
     * 修改来访记录
     *
     * @param model 来访记录
     * @return R 返回修改后的来访记录
     */
    @AutoInject
    @ApiOperation(value = "修改来访记录", notes = "修改来访记录", hidden = true)
    @SysLog("通过id修改来访记录")
    @PutMapping
    public R<ProjectVisitorHis> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<ProjectVisitorHis> model) {
        log.info("[MetaVisitorHisController - update]: 修改来访记录, model={}", JSONConvertUtils.objectToString(model));
        return metaVisitorHistService.update(model.getData());
    }

    /**
     * 通过id删除来访记录
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除来访记录", notes = "通过id删除来访记录", hidden = true)
    @SysLog("通过id删除来访记录")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[MetaVisitorHisController - delete]: 通过id删除来访记录, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return metaVisitorHistService.delete(id);
    }
}
