package com.aurine.cloudx.open.meta.controller;

import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.meta.service.MetaFrameInfoService;
import com.aurine.cloudx.open.origin.entity.ProjectFrameInfo;
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
 * 框架信息管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/meta/frame-info")
@Api(value = "metaFrameInfo", tags = {"v1", "框架数据相关", "框架信息管理"}, hidden = true)
@Inner
@Slf4j
public class MetaFrameInfoController {

    @Resource
    private MetaFrameInfoService metaFrameInfoService;


    /**
     * 新增框架信息
     *
     * @param model 框架信息
     * @return R 返回新增后的框架信息
     */
    @AutoInject
    @ApiOperation(value = "新增框架信息", notes = "新增框架信息", hidden = true)
    @SysLog("新增框架信息")
    @PostMapping
    public R<ProjectFrameInfo> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<ProjectFrameInfo> model) {
        log.info("[MetaFrameInfoController - save]: 新增框架信息, model={}", JSONConvertUtils.objectToString(model));
        return metaFrameInfoService.save(model.getData());
    }

    /**
     * 修改框架信息
     *
     * @param model 框架信息
     * @return R 返回修改后的框架信息
     */
    @AutoInject
    @ApiOperation(value = "修改框架信息", notes = "修改框架信息", hidden = true)
    @SysLog("通过id修改框架信息")
    @PutMapping
    public R<ProjectFrameInfo> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<ProjectFrameInfo> model) {
        log.info("[MetaFrameInfoController - update]: 修改框架信息, model={}", JSONConvertUtils.objectToString(model));
        return metaFrameInfoService.update(model.getData());
    }

    /**
     * 通过id删除框架信息
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除框架信息", notes = "通过id删除框架信息", hidden = true)
    @SysLog("通过id删除框架信息")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[MetaFrameInfoController - delete]: 通过id删除框架信息, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return metaFrameInfoService.delete(id);
    }
}
