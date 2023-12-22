package com.aurine.cloudx.open.meta.controller;

import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.meta.service.MetaPassPlanPolicyRelService;
import com.aurine.cloudx.open.origin.entity.ProjectPassPlanPolicyRel;
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
 * 通行方案策略关系管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/meta/pass-plan-policy-rel")
@Api(value = "metaPassPlanPolicyRel", tags = {"v1", "通行方案相关", "通行方案策略关系管理"}, hidden = true)
@Inner
@Slf4j
public class MetaPassPlanPolicyRelController {

    @Resource
    private MetaPassPlanPolicyRelService metaPassPlanPolicyRelService;


    /**
     * 新增通行方案策略关系
     *
     * @param model 通行方案策略关系
     * @return R 返回新增后的通行方案策略关系
     */
    @AutoInject
    @ApiOperation(value = "新增通行方案策略关系", notes = "新增通行方案策略关系", hidden = true)
    @SysLog("新增通行方案策略关系")
    @PostMapping
    public R<ProjectPassPlanPolicyRel> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<ProjectPassPlanPolicyRel> model) {
        log.info("[MetaPassPlanPolicyRelController - save]: 新增通行方案策略关系, model={}", JSONConvertUtils.objectToString(model));
        return metaPassPlanPolicyRelService.save(model.getData());
    }

    /**
     * 修改通行方案策略关系
     *
     * @param model 通行方案策略关系
     * @return R 返回修改后的通行方案策略关系
     */
    @AutoInject
    @ApiOperation(value = "修改通行方案策略关系", notes = "修改通行方案策略关系", hidden = true)
    @SysLog("通过id修改通行方案策略关系")
    @PutMapping
    public R<ProjectPassPlanPolicyRel> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<ProjectPassPlanPolicyRel> model) {
        log.info("[MetaPassPlanPolicyRelController - update]: 修改通行方案策略关系, model={}", JSONConvertUtils.objectToString(model));
        return metaPassPlanPolicyRelService.update(model.getData());
    }

    /**
     * 通过id删除通行方案策略关系
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除通行方案策略关系", notes = "通过id删除通行方案策略关系", hidden = true)
    @SysLog("通过id删除通行方案策略关系")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[MetaPassPlanPolicyRelController - delete]: 通过id删除通行方案策略关系, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return metaPassPlanPolicyRelService.delete(id);
    }
}
