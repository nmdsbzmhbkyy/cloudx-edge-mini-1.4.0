package com.aurine.cloudx.open.biz.controller;

import com.aurine.cloudx.open.biz.service.OpenUnitInfoService;
import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.entity.model.OpenApiPageModel;
import com.aurine.cloudx.open.common.entity.vo.UnitInfoVo;
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
 * 单元信息管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/open/unit-info")
@Api(value = "openUnitInfo", tags = {"v1", "框架数据相关", "单元信息管理"})
@Inner
@Slf4j
public class OpenUnitInfoController {

    @Resource
    private OpenUnitInfoService openUnitInfoService;


    /**
     * 通过id查询单元信息
     *
     * @param header 请求头信息
     * @param id     要查询的主键id
     * @return R 返回单元信息
     */
    @ApiOperation(value = "通过id查询单元信息", notes = "通过id查询单元信息")
    @SysLog("通过id查询单元信息")
    @GetMapping("/{id}")
    public R<UnitInfoVo> getById(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[OpenUnitInfoController - getById]: 通过id查询单元信息, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return openUnitInfoService.getById(id);
    }

    /**
     * 分页查询单元信息
     *
     * @param pageModel 分页查询条件
     * @return R 返回单元信息分页数据
     */
    @AutoInject
    @ApiOperation(value = "分页查询单元信息", notes = "分页查询单元信息")
    @SysLog("分页查询单元信息")
    @PostMapping("/page")
    public R<Page<UnitInfoVo>> page(@Validated @RequestBody OpenApiPageModel<UnitInfoVo> pageModel) {
        log.info("[OpenUnitInfoController - page]: 分页查询单元信息, page={}, query={}", JSONConvertUtils.objectToString(pageModel.getPage()), JSONConvertUtils.objectToString(pageModel.getData()));
        return openUnitInfoService.page(pageModel.getPage(), pageModel.getData());
    }

    /**
     * 新增单元信息
     *
     * @param model 单元信息
     * @return R 返回新增后的单元信息
     */
    @AutoInject
    @ApiOperation(value = "新增单元信息", notes = "新增单元信息")
    @SysLog("新增单元信息")
    @PostMapping
    public R<UnitInfoVo> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<UnitInfoVo> model) {
        log.info("[OpenUnitInfoController - save]: 新增单元信息, model={}", JSONConvertUtils.objectToString(model));
        return openUnitInfoService.save(model.getData());
    }

    /**
     * 修改单元信息
     *
     * @param model 单元信息
     * @return R 返回修改后的单元信息
     */
    @AutoInject
    @ApiOperation(value = "修改单元信息", notes = "修改单元信息")
    @SysLog("通过id修改单元信息")
    @PutMapping
    public R<UnitInfoVo> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<UnitInfoVo> model) {
        log.info("[OpenUnitInfoController - update]: 修改单元信息, model={}", JSONConvertUtils.objectToString(model));
        return openUnitInfoService.update(model.getData());
    }

    /**
     * 通过id删除单元信息
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除单元信息", notes = "通过id删除单元信息")
    @SysLog("通过id删除单元信息")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[OpenUnitInfoController - delete]: 通过id删除单元信息, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return openUnitInfoService.delete(id);
    }
}
