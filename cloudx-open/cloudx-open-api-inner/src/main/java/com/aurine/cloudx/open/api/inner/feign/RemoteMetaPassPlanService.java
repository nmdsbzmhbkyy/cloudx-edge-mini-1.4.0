package com.aurine.cloudx.open.api.inner.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.origin.entity.ProjectPassPlan;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 通行方案管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@FeignClient(contextId = "remoteMetaPassPlan", value = "cloudx-open-biz")
public interface RemoteMetaPassPlanService {

    /**
     * 新增通行方案
     *
     * @param model 通行方案
     * @return R 返回新增后的通行方案
     */
    @PostMapping("/v1/meta/pass-plan")
    R<ProjectPassPlan> save(@RequestBody OpenApiModel<ProjectPassPlan> model);

    /**
     * 修改通行方案
     *
     * @param model 通行方案
     * @return R 返回修改后的通行方案
     */
    @PutMapping("/v1/meta/pass-plan")
    R<ProjectPassPlan> update(@RequestBody OpenApiModel<ProjectPassPlan> model);

    /**
     * 通过id删除通行方案
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要删除的主键id
     * @return R 返回删除结果
     */
    @DeleteMapping("/v1/meta/pass-plan/{id}")
    R<Boolean> delete(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);
}