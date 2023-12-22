package com.aurine.cloudx.open.meta.controller;

import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
import com.aurine.cloudx.open.meta.service.MetaProjectConfigService;
import com.aurine.cloudx.open.origin.entity.ProjectConfig;
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
 * 项目参数设置
 *
 * @author : zy
 * @date : 2022-11-24 09:50:33
 */
@RestController
@RequestMapping("/v1/meta/config")
@Api(value = "metaConfig", tags = {"v1", "项目参数设置"}, hidden = true)
@Inner
@Slf4j
public class MetaProjectConfigController {

    @Resource
    private MetaProjectConfigService metaProjectConfigService;


    /**
     * 新增项目参数设置
     *
     * @param model 项目参数设置
     * @return R 返回新增后的项目参数设置
     */
    @AutoInject
    @ApiOperation(value = "新增项目参数设置", notes = "新增项目参数设置", hidden = true)
    @SysLog("新增项目参数设置")
    @PostMapping
    public R<ProjectConfig> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<ProjectConfig> model) {
        log.info("[MetaProjectConfigController - save]: 新增项目参数设置, model={}", JSONConvertUtils.objectToString(model));
        return metaProjectConfigService.save(model.getData());
    }

    /**
     * 修改项目参数设置
     *
     * @param model 项目参数设置
     * @return R 返回修改后的项目参数设置
     */
    @AutoInject
    @ApiOperation(value = "修改项目参数设置", notes = "修改项目参数设置", hidden = true)
    @SysLog("修改项目参数设置")
    @PutMapping
    public R<ProjectConfig> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<ProjectConfig> model) {
        log.info("[MetaProjectConfigController - update]: 修改项目参数设置, model={}", JSONConvertUtils.objectToString(model));
        return metaProjectConfigService.update(model.getData());
    }

    /**
     * 通过id删除项目参数设置
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除项目参数设置", notes = "通过id删除项目参数设置", hidden = true)
    @SysLog("通过id删除项目参数设置")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[MetaProjectConfigController - delete]: 通过id删除项目参数设置, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return metaProjectConfigService.delete(id);
    }
}
