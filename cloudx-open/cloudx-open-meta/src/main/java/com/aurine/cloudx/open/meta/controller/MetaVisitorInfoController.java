package com.aurine.cloudx.open.meta.controller;

import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.meta.service.MetaVisitorInfoService;
import com.aurine.cloudx.open.origin.entity.ProjectVisitor;
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
 * 访客信息管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/meta/visitor-info")
@Api(value = "metaVisitorInfo", tags = {"v1", "访客信息管理"}, hidden = true)
@Inner
@Slf4j
public class MetaVisitorInfoController {

    @Resource
    private MetaVisitorInfoService metaVisitorInfoService;


    /**
     * 新增访客信息
     *
     * @param model 访客信息
     * @return R 返回新增后的访客信息
     */
    @AutoInject
    @ApiOperation(value = "新增访客信息", notes = "新增访客信息", hidden = true)
    @SysLog("新增访客信息")
    @PostMapping
    public R<ProjectVisitor> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<ProjectVisitor> model) {
        log.info("[MetaVisitorInfoController - save]: 新增访客信息, model={}", JSONConvertUtils.objectToString(model));
        return metaVisitorInfoService.save(model.getData());
    }

    /**
     * 修改访客信息
     *
     * @param model 访客信息
     * @return R 返回修改后的访客信息
     */
    @AutoInject
    @ApiOperation(value = "修改访客信息", notes = "修改访客信息", hidden = true)
    @SysLog("通过id修改访客信息")
    @PutMapping
    public R<ProjectVisitor> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<ProjectVisitor> model) {
        log.info("[MetaVisitorInfoController - update]: 修改访客信息, model={}", JSONConvertUtils.objectToString(model));
        return metaVisitorInfoService.update(model.getData());
    }

    /**
     * 通过id删除访客信息
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除访客信息", notes = "通过id删除访客信息", hidden = true)
    @SysLog("通过id删除访客信息")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[MetaVisitorInfoController - delete]: 通过id删除访客信息, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return metaVisitorInfoService.delete(id);
    }
}
