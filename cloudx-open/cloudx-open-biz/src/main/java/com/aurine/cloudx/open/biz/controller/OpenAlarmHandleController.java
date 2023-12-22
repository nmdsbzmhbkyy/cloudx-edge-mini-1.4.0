package com.aurine.cloudx.open.biz.controller;

import com.aurine.cloudx.open.biz.service.OpenAlarmHandleService;
import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.entity.model.OpenApiPageModel;
import com.aurine.cloudx.open.common.entity.vo.AlarmHandleVo;
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
 * 报警处理管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/open/alarm-handle")
@Api(value = "openAlarmHandle", tags = {"v1", "报警处理管理"})
@Inner
@Slf4j
public class OpenAlarmHandleController {

    @Resource
    private OpenAlarmHandleService openAlarmHandleService;


    /**
     * 通过id查询报警处理
     *
     * @param header 请求头信息
     * @param id     要查询的主键id
     * @return R 返回报警处理信息
     */
    @ApiOperation(value = "通过id查询报警处理", notes = "通过id查询报警处理")
    @SysLog("通过id查询报警处理")
    @GetMapping("/{id}")
    public R<AlarmHandleVo> getById(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[OpenAlarmHandleController - getById]: 通过id查询报警处理, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return openAlarmHandleService.getById(id);
    }

    /**
     * 分页查询报警处理
     *
     * @param pageModel 分页查询条件
     * @return R 返回报警处理分页数据
     */
    @AutoInject
    @ApiOperation(value = "分页查询报警处理", notes = "分页查询报警处理")
    @SysLog("分页查询报警处理")
    @PostMapping("/page")
    public R<Page<AlarmHandleVo>> page(@Validated @RequestBody OpenApiPageModel<AlarmHandleVo> pageModel) {
        log.info("[OpenAlarmHandleController - page]: 分页查询报警处理, page={}, query={}", JSONConvertUtils.objectToString(pageModel.getPage()), JSONConvertUtils.objectToString(pageModel.getData()));
        return openAlarmHandleService.page(pageModel.getPage(), pageModel.getData());
    }

    /**
     * 新增报警处理
     *
     * @param model 报警处理
     * @return R 返回新增后的报警处理
     */
    @AutoInject
    @ApiOperation(value = "新增报警处理", notes = "新增报警处理")
    @SysLog("新增报警处理")
    @PostMapping
    public R<AlarmHandleVo> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<AlarmHandleVo> model) {
        log.info("[OpenAlarmHandleController - save]: 新增报警处理, model={}", JSONConvertUtils.objectToString(model));
        return openAlarmHandleService.save(model.getData());
    }

    /**
     * 修改报警处理
     *
     * @param model 报警处理
     * @return R 返回修改后的报警处理
     */
    @AutoInject
    @ApiOperation(value = "修改报警处理", notes = "修改报警处理")
    @SysLog("通过id修改报警处理")
    @PutMapping
    public R<AlarmHandleVo> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<AlarmHandleVo> model) {
        log.info("[OpenAlarmHandleController - update]: 修改报警处理, model={}", JSONConvertUtils.objectToString(model));
        return openAlarmHandleService.update(model.getData());
    }

    /**
     * 通过id删除报警处理
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除报警处理", notes = "通过id删除报警处理")
    @SysLog("通过id删除报警处理")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[OpenAlarmHandleController - delete]: 通过id删除报警处理, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return openAlarmHandleService.delete(id);
    }
}
