package com.aurine.cloudx.open.biz.controller;

import com.aurine.cloudx.open.biz.service.OpenVisitorHisService;
import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.entity.model.OpenApiPageModel;
import com.aurine.cloudx.open.common.entity.vo.VisitorHisVo;
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
 * 来访记录管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/open/visitor-his")
@Api(value = "openVisitorHis", tags = {"v1", "来访记录管理"})
@Inner
@Slf4j
public class OpenVisitorHisController {

    @Resource
    private OpenVisitorHisService openVisitorHisService;


    /**
     * 通过id查询来访记录
     *
     * @param header 请求头信息
     * @param id     要查询的主键id
     * @return R 返回来访记录信息
     */
    @ApiOperation(value = "通过id查询来访记录", notes = "通过id查询来访记录")
    @SysLog("通过id查询来访记录")
    @GetMapping("/{id}")
    public R<VisitorHisVo> getById(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[OpenVisitorHisController - getById]: 通过id查询来访记录, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return openVisitorHisService.getById(id);
    }

    /**
     * 分页查询来访记录
     *
     * @param pageModel 分页查询条件
     * @return R 返回来访记录分页数据
     */
    @AutoInject
    @ApiOperation(value = "分页查询来访记录", notes = "分页查询来访记录")
    @SysLog("分页查询来访记录")
    @PostMapping("/page")
    public R<Page<VisitorHisVo>> page(@Validated @RequestBody OpenApiPageModel<VisitorHisVo> pageModel) {
        log.info("[OpenVisitorHisController - page]: 分页查询来访记录, page={}, query={}", JSONConvertUtils.objectToString(pageModel.getPage()), JSONConvertUtils.objectToString(pageModel.getData()));
        return openVisitorHisService.page(pageModel.getPage(), pageModel.getData());
    }

    /**
     * 新增来访记录
     *
     * @param model 来访记录
     * @return R 返回新增后的来访记录
     */
    @AutoInject
    @ApiOperation(value = "新增来访记录", notes = "新增来访记录")
    @SysLog("新增来访记录")
    @PostMapping
    public R<VisitorHisVo> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<VisitorHisVo> model) {
        log.info("[OpenVisitorHisController - save]: 新增来访记录, model={}", JSONConvertUtils.objectToString(model));
        return openVisitorHisService.save(model.getData());
    }

    /**
     * 修改来访记录
     *
     * @param model 来访记录
     * @return R 返回修改后的来访记录
     */
    @AutoInject
    @ApiOperation(value = "修改来访记录", notes = "修改来访记录")
    @SysLog("通过id修改来访记录")
    @PutMapping
    public R<VisitorHisVo> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<VisitorHisVo> model) {
        log.info("[OpenVisitorHisController - update]: 修改来访记录, model={}", JSONConvertUtils.objectToString(model));
        return openVisitorHisService.update(model.getData());
    }

    /**
     * 通过id删除来访记录
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除来访记录", notes = "通过id删除来访记录")
    @SysLog("通过id删除来访记录")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[OpenVisitorHisController - delete]: 通过id删除来访记录, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return openVisitorHisService.delete(id);
    }
}
