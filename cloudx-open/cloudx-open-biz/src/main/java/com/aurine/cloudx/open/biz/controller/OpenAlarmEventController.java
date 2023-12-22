package com.aurine.cloudx.open.biz.controller;

import com.aurine.cloudx.open.biz.service.OpenAlarmEventService;
import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.entity.model.OpenApiPageModel;
import com.aurine.cloudx.open.common.entity.vo.AlarmEventVo;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
@RequestMapping("/v1/open/alarm-event")
@Api(value = "openAlarmEvent", tags = {"v1", "报警事件管理"})
@Inner
@Slf4j
public class OpenAlarmEventController {

    @Resource
    private OpenAlarmEventService openAlarmEventService;


    /**
     * 通过id查询报警事件
     *
     * @param header 请求头信息
     * @param id     要查询的主键id
     * @return R 返回报警事件信息
     */
    @ApiOperation(value = "通过id查询报警事件", notes = "通过id查询报警事件")
    @SysLog("通过id查询报警事件")
    @GetMapping("/{id}")
    public R<AlarmEventVo> getById(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[OpenAlarmEventController - getById]: 通过id查询报警事件, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return openAlarmEventService.getById(id);
    }

    /**
     * 分页查询报警事件
     *
     * @param pageModel 分页查询条件
     * @return R 返回报警事件分页数据
     */
    @AutoInject
    @ApiOperation(value = "分页查询报警事件", notes = "分页查询报警事件")
    @SysLog("分页查询报警事件")
    @PostMapping("/page")
    public R<Page<AlarmEventVo>> page(@Validated @RequestBody OpenApiPageModel<AlarmEventVo> pageModel) {
        log.info("[OpenAlarmEventController - page]: 分页查询报警事件, page={}, query={}", JSONConvertUtils.objectToString(pageModel.getPage()), JSONConvertUtils.objectToString(pageModel.getData()));
        return openAlarmEventService.page(pageModel.getPage(), pageModel.getData());
    }

    /**
     * 新增报警事件
     *
     * @param model 报警事件
     * @return R 返回新增后的报警事件
     */
    @AutoInject
    @ApiOperation(value = "新增报警事件", notes = "新增报警事件")
    @SysLog("新增报警事件")
    @PostMapping
    public R<AlarmEventVo> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<AlarmEventVo> model) {
        log.info("[OpenAlarmEventController - save]: 新增报警事件, model={}", JSONConvertUtils.objectToString(model));
        return openAlarmEventService.save(model.getData());
    }

    /**
     * 修改报警事件
     *
     * @param model 报警事件
     * @return R 返回修改后的报警事件
     */
    @AutoInject
    @ApiOperation(value = "修改报警事件", notes = "修改报警事件")
    @SysLog("通过id修改报警事件")
    @PutMapping
    public R<AlarmEventVo> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<AlarmEventVo> model) {
        log.info("[OpenAlarmEventController - update]: 修改报警事件, model={}", JSONConvertUtils.objectToString(model));
        return openAlarmEventService.update(model.getData());
    }

    /**
     * 通过id删除报警事件
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除报警事件", notes = "通过id删除报警事件")
    @SysLog("通过id删除报警事件")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[OpenAlarmEventController - delete]: 通过id删除报警事件, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return openAlarmEventService.delete(id);
    }
}
