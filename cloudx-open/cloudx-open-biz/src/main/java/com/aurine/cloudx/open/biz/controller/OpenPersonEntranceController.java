package com.aurine.cloudx.open.biz.controller;

import com.aurine.cloudx.open.biz.service.OpenPersonEntranceService;
import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.entity.model.OpenApiPageModel;
import com.aurine.cloudx.open.common.entity.vo.PersonEntranceVo;
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
 * 人行事件管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/open/person-entrance")
@Api(value = "openPersonEntrance", tags = {"v1", "人行事件管理"})
@Inner
@Slf4j
public class OpenPersonEntranceController {

    @Resource
    private OpenPersonEntranceService openPersonEntranceService;


    /**
     * 通过id查询人行事件
     *
     * @param header 请求头信息
     * @param id     要查询的主键id
     * @return R 返回人行事件信息
     */
    @ApiOperation(value = "通过id查询人行事件", notes = "通过id查询人行事件")
    @SysLog("通过id查询人行事件")
    @GetMapping("/{id}")
    public R<PersonEntranceVo> getById(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[OpenPersonEntranceController - getById]: 通过id查询人行事件, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return openPersonEntranceService.getById(id);
    }

    /**
     * 分页查询人行事件
     *
     * @param pageModel 分页查询条件
     * @return R 返回人行事件分页数据
     */
    @AutoInject
    @ApiOperation(value = "分页查询人行事件", notes = "分页查询人行事件")
    @SysLog("分页查询人行事件")
    @PostMapping("/page")
    public R<Page<PersonEntranceVo>> page(@Validated @RequestBody OpenApiPageModel<PersonEntranceVo> pageModel) {
        log.info("[OpenPersonEntranceController - page]: 分页查询人行事件, page={}, query={}", JSONConvertUtils.objectToString(pageModel.getPage()), JSONConvertUtils.objectToString(pageModel.getData()));
        return openPersonEntranceService.page(pageModel.getPage(), pageModel.getData());
    }

    /**
     * 新增人行事件
     *
     * @param model 人行事件
     * @return R 返回新增后的人行事件
     */
    @AutoInject
    @ApiOperation(value = "新增人行事件", notes = "新增人行事件")
    @SysLog("新增人行事件")
    @PostMapping
    public R<PersonEntranceVo> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<PersonEntranceVo> model) {
        log.info("[OpenPersonEntranceController - save]: 新增人行事件, model={}", JSONConvertUtils.objectToString(model));
        return openPersonEntranceService.save(model.getData());
    }

    /**
     * 修改人行事件
     *
     * @param model 人行事件
     * @return R 返回修改后的人行事件
     */
    @AutoInject
    @ApiOperation(value = "修改人行事件", notes = "修改人行事件")
    @SysLog("通过id修改人行事件")
    @PutMapping
    public R<PersonEntranceVo> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<PersonEntranceVo> model) {
        log.info("[OpenPersonEntranceController - update]: 修改人行事件, model={}", JSONConvertUtils.objectToString(model));
        return openPersonEntranceService.update(model.getData());
    }

    /**
     * 通过id删除人行事件
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除人行事件", notes = "通过id删除人行事件")
    @SysLog("通过id删除人行事件")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[OpenPersonEntranceController - delete]: 通过id删除人行事件, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return openPersonEntranceService.delete(id);
    }
}
