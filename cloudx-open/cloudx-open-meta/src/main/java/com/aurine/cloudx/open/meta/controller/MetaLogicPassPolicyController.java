package com.aurine.cloudx.open.meta.controller;

import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.meta.service.MetaLogicPassPolicyService;
import com.aurine.cloudx.open.origin.entity.ProjectLogicPassPolicy;
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
 * 逻辑策略管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/meta/logic-pass-policy")
@Api(value = "metaLogicPassPolicy", tags = {"v1", "通行方案相关", "逻辑策略管理"}, hidden = true)
@Inner
@Slf4j
public class MetaLogicPassPolicyController {

    @Resource
    private MetaLogicPassPolicyService metaLogicPassPolicyService;


    /**
     * 新增逻辑策略
     *
     * @param model 逻辑策略
     * @return R 返回新增后的逻辑策略
     */
    @AutoInject
    @ApiOperation(value = "新增逻辑策略", notes = "新增逻辑策略", hidden = true)
    @SysLog("新增逻辑策略")
    @PostMapping
    public R<ProjectLogicPassPolicy> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<ProjectLogicPassPolicy> model) {
        log.info("[MetaLogicPassPolicyController - save]: 新增逻辑策略, model={}", JSONConvertUtils.objectToString(model));
        return metaLogicPassPolicyService.save(model.getData());
    }

    /**
     * 修改逻辑策略
     *
     * @param model 逻辑策略
     * @return R 返回修改后的逻辑策略
     */
    @AutoInject
    @ApiOperation(value = "修改逻辑策略", notes = "修改逻辑策略", hidden = true)
    @SysLog("通过id修改逻辑策略")
    @PutMapping
    public R<ProjectLogicPassPolicy> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<ProjectLogicPassPolicy> model) {
        log.info("[MetaLogicPassPolicyController - update]: 修改逻辑策略, model={}", JSONConvertUtils.objectToString(model));
        return metaLogicPassPolicyService.update(model.getData());
    }

    /**
     * 通过id删除逻辑策略
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除逻辑策略", notes = "通过id删除逻辑策略", hidden = true)
    @SysLog("通过id删除逻辑策略")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[MetaLogicPassPolicyController - delete]: 通过id删除逻辑策略, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return metaLogicPassPolicyService.delete(id);
    }
}
