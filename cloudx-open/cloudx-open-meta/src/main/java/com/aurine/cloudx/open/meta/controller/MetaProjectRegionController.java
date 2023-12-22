package com.aurine.cloudx.open.meta.controller;

import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.meta.service.MetaProjectRegionService;
import com.aurine.cloudx.open.origin.entity.ProjectDeviceRegion;
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
 * 项目区域管理
 *
 * @author xull@aurine.cn
 * @date 2020-05-25 08:50:24
 */

@RestController
@RequestMapping("/v1/meta/project-region")
@Api(value = "metaProjectRegion", tags = {"v1", "基础数据相关", "项目区域管理"}, hidden = true)
@Inner
@Slf4j
public class MetaProjectRegionController {

    @Resource
    private MetaProjectRegionService metaProjectRegionService;


    /**
     * 新增项目区域
     *
     * @param model 项目区域
     * @return R 返回新增后的项目区域
     */
    @AutoInject
    @ApiOperation(value = "新增项目区域", notes = "新增项目区域", hidden = true)
    @SysLog("新增项目区域")
    @PostMapping
    public R<ProjectDeviceRegion> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<ProjectDeviceRegion> model) {
        log.info("[MetaProjectRegionController - save]: 新增项目区域, model={}", JSONConvertUtils.objectToString(model));
        return metaProjectRegionService.save(model.getData());
    }

    /**
     * 修改项目区域
     *
     * @param model 项目区域
     * @return R 返回修改后的项目区域
     */
    @AutoInject
    @ApiOperation(value = "修改项目区域", notes = "修改项目区域", hidden = true)
    @SysLog("修改项目区域")
    @PutMapping
    public R<ProjectDeviceRegion> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<ProjectDeviceRegion> model) {
        log.info("[MetaProjectRegionController - update]: 修改项目区域, model={}", JSONConvertUtils.objectToString(model));
        return metaProjectRegionService.update(model.getData());
    }

    /**
     * 通过id删除项目区域
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除项目区域", notes = "通过id删除项目区域", hidden = true)
    @SysLog("通过id删除项目区域")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[MetaProjectRegionController - delete]: 通过id删除项目区域, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return metaProjectRegionService.delete(id);
    }
}
