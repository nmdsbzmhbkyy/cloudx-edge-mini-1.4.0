package com.aurine.cloudx.open.meta.controller;

import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.meta.service.MetaPersonPlanRelService;
import com.aurine.cloudx.open.origin.entity.ProjectPersonPlanRel;
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
 * 人员通行方案关系管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/meta/person-plan-rel")
@Api(value = "metaPersonPlanRel", tags = {"v1", "通行方案相关", "人员通行方案关系管理"}, hidden = true)
@Inner
@Slf4j
public class MetaPersonPlanRelController {

    @Resource
    private MetaPersonPlanRelService metaPersonPlanRelService;


    /**
     * 新增人员通行方案关系
     *
     * @param model 人员通行方案关系
     * @return R 返回新增后的人员通行方案关系
     */
    @AutoInject
    @ApiOperation(value = "新增人员通行方案关系", notes = "新增人员通行方案关系", hidden = true)
    @SysLog("新增人员通行方案关系")
    @PostMapping
    public R<ProjectPersonPlanRel> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<ProjectPersonPlanRel> model) {
        log.info("[MetaPersonPlanRelController - save]: 新增人员通行方案关系, model={}", JSONConvertUtils.objectToString(model));
        return metaPersonPlanRelService.save(model.getData());
    }

    /**
     * 修改人员通行方案关系
     *
     * @param model 人员通行方案关系
     * @return R 返回修改后的人员通行方案关系
     */
    @AutoInject
    @ApiOperation(value = "修改人员通行方案关系", notes = "修改人员通行方案关系", hidden = true)
    @SysLog("通过id修改人员通行方案关系")
    @PutMapping
    public R<ProjectPersonPlanRel> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<ProjectPersonPlanRel> model) {
        log.info("[MetaPersonPlanRelController - update]: 修改人员通行方案关系, model={}", JSONConvertUtils.objectToString(model));
        return metaPersonPlanRelService.update(model.getData());
    }

    /**
     * 通过id删除人员通行方案关系
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除人员通行方案关系", notes = "通过id删除人员通行方案关系", hidden = true)
    @SysLog("通过id删除人员通行方案关系")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[MetaPersonPlanRelController - delete]: 通过id删除人员通行方案关系, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return metaPersonPlanRelService.delete(id);
    }
}
