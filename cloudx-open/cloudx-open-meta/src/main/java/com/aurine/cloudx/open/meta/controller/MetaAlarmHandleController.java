package com.aurine.cloudx.open.meta.controller;

import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.meta.service.MetaAlarmHandleService;
import com.aurine.cloudx.open.origin.entity.ProjectAlarmHandle;
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
 * 报警处理管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/meta/alarm-handle")
@Api(value = "metaAlarmHandle", tags = {"v1", "报警处理管理"}, hidden = true)
@Inner
@Slf4j
public class MetaAlarmHandleController {

    @Resource
    private MetaAlarmHandleService metaAlarmHandleService;


    /**
     * 新增报警处理
     *
     * @param model 报警处理
     * @return R 返回新增后的报警处理
     */
    @AutoInject
    @ApiOperation(value = "新增报警处理", notes = "新增报警处理", hidden = true)
    @SysLog("新增报警处理")
    @PostMapping
    public R<ProjectAlarmHandle> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<ProjectAlarmHandle> model) {
        log.info("[MetaAlarmHandleController - save]: 新增报警处理, model={}", JSONConvertUtils.objectToString(model));
        return metaAlarmHandleService.save(model.getData());
    }

    /**
     * 修改报警处理
     *
     * @param model 报警处理
     * @return R 返回修改后的报警处理
     */
    @AutoInject
    @ApiOperation(value = "修改报警处理", notes = "修改报警处理", hidden = true)
    @SysLog("通过id修改报警处理")
    @PutMapping
    public R<ProjectAlarmHandle> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<ProjectAlarmHandle> model) {
        log.info("[MetaAlarmHandleController - update]: 修改报警处理, model={}", JSONConvertUtils.objectToString(model));
        return metaAlarmHandleService.update(model.getData());
    }

    /**
     * 通过id删除报警处理
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除报警处理", notes = "通过id删除报警处理", hidden = true)
    @SysLog("通过id删除报警处理")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[MetaAlarmHandleController - delete]: 通过id删除报警处理, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return metaAlarmHandleService.delete(id);
    }
}
