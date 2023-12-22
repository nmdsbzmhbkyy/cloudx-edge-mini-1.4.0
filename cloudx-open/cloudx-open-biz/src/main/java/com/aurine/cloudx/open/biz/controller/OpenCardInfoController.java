package com.aurine.cloudx.open.biz.controller;

import com.aurine.cloudx.open.biz.service.OpenCardInfoService;
import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.entity.model.OpenApiPageModel;
import com.aurine.cloudx.open.common.entity.vo.CardInfoVo;
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
 * 卡信息管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/open/card-info")
@Api(value = "openCardInfo", tags = {"v1", "卡信息管理"})
@Inner
@Slf4j
public class OpenCardInfoController {

    @Resource
    private OpenCardInfoService openCardInfoService;


    /**
     * 通过id查询卡信息
     *
     * @param header 请求头信息
     * @param id     要查询的主键id
     * @return R 返回卡信息
     */
    @ApiOperation(value = "通过id查询卡信息", notes = "通过id查询卡信息")
    @SysLog("通过id查询卡信息")
    @GetMapping("/{id}")
    public R<CardInfoVo> getById(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[OpenCardInfoController - getById]: 通过id查询卡信息, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return openCardInfoService.getById(id);
    }

    /**
     * 分页查询卡信息
     *
     * @param pageModel 分页查询条件
     * @return R 返回卡信息分页数据
     */
    @AutoInject
    @ApiOperation(value = "分页查询卡信息", notes = "分页查询卡信息")
    @SysLog("分页查询卡信息")
    @PostMapping("/page")
    public R<Page<CardInfoVo>> page(@Validated @RequestBody OpenApiPageModel<CardInfoVo> pageModel) {
        log.info("[OpenCardInfoController - page]: 分页查询卡信息, page={}, query={}", JSONConvertUtils.objectToString(pageModel.getPage()), JSONConvertUtils.objectToString(pageModel.getData()));
        return openCardInfoService.page(pageModel.getPage(), pageModel.getData());
    }

    /**
     * 新增卡信息
     *
     * @param model 卡信息
     * @return R 返回新增后的卡信息
     */
    @AutoInject
    @ApiOperation(value = "新增卡信息", notes = "新增卡信息")
    @SysLog("新增卡信息")
    @PostMapping
    public R<CardInfoVo> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<CardInfoVo> model) {
        log.info("[OpenCardInfoController - save]: 新增卡信息, model={}", JSONConvertUtils.objectToString(model));
        return openCardInfoService.save(model.getData());
    }

    /**
     * 修改卡信息
     *
     * @param model 卡信息
     * @return R 返回修改后的卡信息
     */
    @AutoInject
    @ApiOperation(value = "修改卡信息", notes = "修改卡信息")
    @SysLog("通过id修改卡信息")
    @PutMapping
    public R<CardInfoVo> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<CardInfoVo> model) {
        log.info("[OpenCardInfoController - update]: 修改卡信息, model={}", JSONConvertUtils.objectToString(model));
        return openCardInfoService.update(model.getData());
    }

    /**
     * 通过id删除卡信息
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除卡信息", notes = "通过id删除卡信息")
    @SysLog("通过id删除卡信息")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[OpenCardInfoController - delete]: 通过id删除卡信息, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return openCardInfoService.delete(id);
    }
}
