package com.aurine.cloudx.open.meta.controller;

import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.meta.service.MetaEntityLevelCfgService;
import com.aurine.cloudx.open.origin.entity.ProjectEntityLevelCfg;
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
@RequestMapping("/v1/meta/entity-level-cfg")
@Api(value = "metaEntityLevelCfg", tags = {"v1", "框架数据相关", "组团配置管理"}, hidden = true)
@Inner
@Slf4j
public class MetaEntityLevelCfgController {

    @Resource
    private MetaEntityLevelCfgService metaEntityLevelCfgService;


    /**
     * 新增组团配置
     * 注：组团配置可能没有新增功能，该接口只是统一定义后的结果
     *
     * @param model 组团配置
     * @return R 返回新增后的组团配置
     */
    @AutoInject
    @ApiOperation(value = "新增组团配置", notes = "新增组团配置", hidden = true)
    @SysLog("新增组团配置")
    @PostMapping
    public R<ProjectEntityLevelCfg> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<ProjectEntityLevelCfg> model) {
        log.info("[MetaEntityLevelCfgController - save]: 新增组团配置, model={}", JSONConvertUtils.objectToString(model));
        return metaEntityLevelCfgService.save(model.getData());
    }

    /**
     * 修改组团配置
     *
     * @param model 组团配置
     * @return R 返回修改后的组团配置
     */
    @AutoInject
    @ApiOperation(value = "修改组团配置", notes = "修改组团配置", hidden = true)
    @SysLog("通过id修改组团配置")
    @PutMapping
    public R<ProjectEntityLevelCfg> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<ProjectEntityLevelCfg> model) {
        log.info("[MetaEntityLevelCfgController - update]: 修改组团配置, model={}", JSONConvertUtils.objectToString(model));
        return metaEntityLevelCfgService.update(model.getData());
    }

    /**
     * 通过id删除组团配置
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除组团配置", notes = "通过id删除组团配置", hidden = true)
    @SysLog("通过id删除组团配置")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[MetaEntityLevelCfgController - delete]: 通过id删除组团配置, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return metaEntityLevelCfgService.delete(id);
    }
}
