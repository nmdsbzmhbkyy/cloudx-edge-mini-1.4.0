package com.aurine.cloudx.open.api.inner.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.origin.entity.ProjectPassPlanPolicyRel;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 通行方案策略关系管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@FeignClient(contextId = "remoteMetaPassPlanPolicyRel", value = "cloudx-open-biz")
public interface RemoteMetaPassPlanPolicyRelService {

    /**
     * 新增通行方案策略关系
     *
     * @param model 通行方案策略关系
     * @return R 返回新增后的通行方案策略关系
     */
    @PostMapping("/v1/meta/pass-plan-policy-rel")
    R<ProjectPassPlanPolicyRel> save(@RequestBody OpenApiModel<ProjectPassPlanPolicyRel> model);

    /**
     * 修改通行方案策略关系
     *
     * @param model 通行方案策略关系
     * @return R 返回修改后的通行方案策略关系
     */
    @PutMapping("/v1/meta/pass-plan-policy-rel")
    R<ProjectPassPlanPolicyRel> update(@RequestBody OpenApiModel<ProjectPassPlanPolicyRel> model);

    /**
     * 通过id删除通行方案策略关系
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要删除的主键id
     * @return R 返回删除结果
     */
    @DeleteMapping("/v1/meta/pass-plan-policy-rel/{id}")
    R<Boolean> delete(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);
}