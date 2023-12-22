package com.aurine.cloudx.open.biz.controller;

import com.aurine.cloudx.open.biz.service.OpenEntityLevelCfgService;
import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.entity.model.OpenApiPageModel;
import com.aurine.cloudx.open.common.entity.vo.EntityLevelCfgVo;
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
 * 组团配置管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/open/entity-level-cfg")
@Api(value = "openEntityLevelCfg", tags = {"v1", "框架数据相关", "组团配置管理"})
@Inner
@Slf4j
public class OpenEntityLevelCfgController {

    @Resource
    private OpenEntityLevelCfgService openEntityLevelCfgService;


    /**
     * 通过id查询组团配置
     *
     * @param header 请求头信息
     * @param id     要查询的主键id
     * @return R 返回组团配置信息
     */
    @ApiOperation(value = "通过id查询组团配置", notes = "通过id查询组团配置")
    @SysLog("通过id查询组团配置")
    @GetMapping("/{id}")
    public R<EntityLevelCfgVo> getById(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[OpenEntityLevelCfgController - getById]: 通过id查询组团配置, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return openEntityLevelCfgService.getById(id);
    }

    /**
     * 分页查询组团配置
     *
     * @param pageModel 分页查询条件
     * @return R 返回组团配置分页数据
     */
    @AutoInject
    @ApiOperation(value = "分页查询组团配置", notes = "分页查询组团配置")
    @SysLog("分页查询组团配置")
    @PostMapping("/page")
    public R<Page<EntityLevelCfgVo>> page(@Validated @RequestBody OpenApiPageModel<EntityLevelCfgVo> pageModel) {
        log.info("[OpenEntityLevelCfgController - page]: 分页查询组团配置, page={}, query={}", JSONConvertUtils.objectToString(pageModel.getPage()), JSONConvertUtils.objectToString(pageModel.getData()));
        return openEntityLevelCfgService.page(pageModel.getPage(), pageModel.getData());
    }

    /**
     * 新增组团配置
     * 注：组团配置可能没有新增功能，该接口只是统一定义后的结果
     *
     * @param model 组团配置
     * @return R 返回新增后的组团配置
     */
    @AutoInject
    @ApiOperation(value = "新增组团配置", notes = "新增组团配置")
    @SysLog("新增组团配置")
    @PostMapping
    public R<EntityLevelCfgVo> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<EntityLevelCfgVo> model) {
        log.info("[OpenEntityLevelCfgController - save]: 新增组团配置, model={}", JSONConvertUtils.objectToString(model));
        return openEntityLevelCfgService.save(model.getData());
    }

    /**
     * 修改组团配置
     *
     * @param model 组团配置
     * @return R 返回修改后的组团配置
     */
    @AutoInject
    @ApiOperation(value = "修改组团配置", notes = "修改组团配置")
    @SysLog("通过id修改组团配置")
    @PutMapping
    public R<EntityLevelCfgVo> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<EntityLevelCfgVo> model) {
        log.info("[OpenEntityLevelCfgController - update]: 修改组团配置, model={}", JSONConvertUtils.objectToString(model));
        return openEntityLevelCfgService.update(model.getData());
    }

    /**
     * 通过id删除组团配置
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除组团配置", notes = "通过id删除组团配置")
    @SysLog("通过id删除组团配置")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[OpenEntityLevelCfgController - delete]: 通过id删除组团配置, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return openEntityLevelCfgService.delete(id);
    }
}
