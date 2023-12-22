package com.aurine.cloudx.open.meta.controller;

import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.meta.service.MetaPhysicalPassPolicyService;
import com.aurine.cloudx.open.origin.entity.ProjectPhysicalPassPolicy;
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
 * 物理策略管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/meta/physical-pass-policy")
@Api(value = "metaPhysicalPassPolicy", tags = {"v1", "通行方案相关", "物理策略管理"}, hidden = true)
@Inner
@Slf4j
public class MetaPhysicalPassPolicyController {

    @Resource
    private MetaPhysicalPassPolicyService metaPhysicalPassPolicyService;


    /**
     * 新增物理策略
     *
     * @param model 物理策略
     * @return R 返回新增后的物理策略
     */
    @AutoInject
    @ApiOperation(value = "新增物理策略", notes = "新增物理策略", hidden = true)
    @SysLog("新增物理策略")
    @PostMapping
    public R<ProjectPhysicalPassPolicy> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<ProjectPhysicalPassPolicy> model) {
        log.info("[MetaPhysicalPassPolicyController - save]: 新增物理策略, model={}", JSONConvertUtils.objectToString(model));
        return metaPhysicalPassPolicyService.save(model.getData());
    }

    /**
     * 修改物理策略
     *
     * @param model 物理策略
     * @return R 返回修改后的物理策略
     */
    @AutoInject
    @ApiOperation(value = "修改物理策略", notes = "修改物理策略", hidden = true)
    @SysLog("通过id修改物理策略")
    @PutMapping
    public R<ProjectPhysicalPassPolicy> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<ProjectPhysicalPassPolicy> model) {
        log.info("[MetaPhysicalPassPolicyController - update]: 修改物理策略, model={}", JSONConvertUtils.objectToString(model));
        return metaPhysicalPassPolicyService.update(model.getData());
    }

    /**
     * 通过id删除物理策略
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除物理策略", notes = "通过id删除物理策略", hidden = true)
    @SysLog("通过id删除物理策略")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[MetaPhysicalPassPolicyController - delete]: 通过id删除物理策略, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return metaPhysicalPassPolicyService.delete(id);
    }
}
