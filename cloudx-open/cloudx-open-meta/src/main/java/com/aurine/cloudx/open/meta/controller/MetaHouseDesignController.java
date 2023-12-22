package com.aurine.cloudx.open.meta.controller;

import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.meta.service.MetaHouseDesignService;
import com.aurine.cloudx.open.origin.entity.ProjectHouseDesign;
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
 * 户型管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/meta/house-design")
@Api(value = "metaHouseDesign", tags = {"v1", "基础数据相关", "户型管理"}, hidden = true)
@Inner
@Slf4j
public class MetaHouseDesignController {

    @Resource
    private MetaHouseDesignService metaHouseDesignService;


    /**
     * 新增户型
     *
     * @param model 户型信息
     * @return R 返回新增后的户型信息
     */
    @AutoInject
    @ApiOperation(value = "新增户型", notes = "新增户型", hidden = true)
    @SysLog("新增户型")
    @PostMapping
    public R<ProjectHouseDesign> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<ProjectHouseDesign> model) {
        log.info("[MetaHouseDesignController - save]: 新增户型, model={}", JSONConvertUtils.objectToString(model));
        return metaHouseDesignService.save(model.getData());
    }

    /**
     * 修改户型
     *
     * @param model 户型信息
     * @return R 返回修改后的户型信息
     */
    @AutoInject
    @ApiOperation(value = "修改户型", notes = "修改户型", hidden = true)
    @SysLog("通过id修改户型")
    @PutMapping
    public R<ProjectHouseDesign> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<ProjectHouseDesign> model) {
        log.info("[MetaHouseDesignController - update]: 修改户型, model={}", JSONConvertUtils.objectToString(model));
        return metaHouseDesignService.update(model.getData());
    }

    /**
     * 通过id删除户型
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除户型", notes = "通过id删除户型", hidden = true)
    @SysLog("通过id删除户型")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[MetaHouseDesignController - delete]: 通过id删除户型, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return metaHouseDesignService.delete(id);
    }
}
