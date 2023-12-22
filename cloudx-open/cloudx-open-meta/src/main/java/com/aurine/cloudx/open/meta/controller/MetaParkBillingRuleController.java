package com.aurine.cloudx.open.meta.controller;

import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
import com.aurine.cloudx.open.meta.service.MetaParkBillingRuleService;
import com.aurine.cloudx.open.origin.entity.ProjectParkBillingRule;
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
 * 车场计费规则管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/meta/park-billing-rule")
@Api(value = "metaParkBillingRule", tags = {"v1", "车场计费规则管理"}, hidden = true)
@Inner
@Slf4j
public class MetaParkBillingRuleController {

    @Resource
    private MetaParkBillingRuleService metaParkBillingRuleService;


    /**
     * 新增车场计费规则
     *
     * @param model 车场计费规则
     * @return R 返回新增后的车场计费规则
     */
    @AutoInject
    @ApiOperation(value = "新增车场计费规则", notes = "新增车场计费规则", hidden = true)
    @SysLog("新增车场计费规则")
    @PostMapping
    public R<ProjectParkBillingRule> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<ProjectParkBillingRule> model) {
        log.info("[MetaParkBillingRuleController - save]: 新增车场计费规则, model={}", JSONConvertUtils.objectToString(model));
        return metaParkBillingRuleService.save(model.getData());
    }

    /**
     * 修改车场计费规则
     *
     * @param model 车场计费规则
     * @return R 返回修改后的车场计费规则
     */
    @AutoInject
    @ApiOperation(value = "修改车场计费规则", notes = "修改车场计费规则", hidden = true)
    @SysLog("通过id修改车场计费规则")
    @PutMapping
    public R<ProjectParkBillingRule> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<ProjectParkBillingRule> model) {
        log.info("[MetaParkBillingRuleController - update]: 修改车场计费规则, model={}", JSONConvertUtils.objectToString(model));
        return metaParkBillingRuleService.update(model.getData());
    }

    /**
     * 通过id删除车场计费规则
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除车场计费规则", notes = "通过id删除车场计费规则", hidden = true)
    @SysLog("通过id删除车场计费规则")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[MetaParkBillingRuleController - delete]: 通过id删除车场计费规则, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return metaParkBillingRuleService.delete(id);
    }
}
