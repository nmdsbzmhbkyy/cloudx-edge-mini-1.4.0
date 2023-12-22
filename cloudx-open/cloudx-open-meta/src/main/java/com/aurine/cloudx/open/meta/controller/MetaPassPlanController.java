package com.aurine.cloudx.open.meta.controller;

import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.meta.service.MetaPassPlanService;
import com.aurine.cloudx.open.origin.entity.ProjectPassPlan;
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
 * 通行方案管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/meta/pass-plan")
@Api(value = "metaPassPlan", tags = {"v1", "通行方案相关", "通行方案管理"}, hidden = true)
@Inner
@Slf4j
public class MetaPassPlanController {

    @Resource
    private MetaPassPlanService metaPassPlanService;


    /**
     * 新增通行方案
     *
     * @param model 通行方案
     * @return R 返回新增后的通行方案
     */
    @AutoInject
    @ApiOperation(value = "新增通行方案", notes = "新增通行方案", hidden = true)
    @SysLog("新增通行方案")
    @PostMapping
    public R<ProjectPassPlan> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<ProjectPassPlan> model) {
        log.info("[MetaPassPlanController - save]: 新增通行方案, model={}", JSONConvertUtils.objectToString(model));
        return metaPassPlanService.save(model.getData());
    }

    /**
     * 修改通行方案
     *
     * @param model 通行方案
     * @return R 返回修改后的通行方案
     */
    @AutoInject
    @ApiOperation(value = "修改通行方案", notes = "修改通行方案", hidden = true)
    @SysLog("通过id修改通行方案")
    @PutMapping
    public R<ProjectPassPlan> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<ProjectPassPlan> model) {
        log.info("[MetaPassPlanController - update]: 修改通行方案, model={}", JSONConvertUtils.objectToString(model));
        return metaPassPlanService.update(model.getData());
    }

    /**
     * 通过id删除通行方案
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除通行方案", notes = "通过id删除通行方案", hidden = true)
    @SysLog("通过id删除通行方案")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[MetaPassPlanController - delete]: 通过id删除通行方案, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return metaPassPlanService.delete(id);
    }
}
