package com.aurine.cloudx.open.api.inner.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.origin.entity.ProjectPersonPlanRel;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 人员通行方案关系管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@FeignClient(contextId = "remoteMetaPersonPlanRel", value = "cloudx-open-biz")
public interface RemoteMetaPersonPlanRelService {

    /**
     * 新增人员通行方案关系
     *
     * @param model 人员通行方案关系
     * @return R 返回新增后的人员通行方案关系
     */
    @PostMapping("/v1/meta/person-plan-rel")
    R<ProjectPersonPlanRel> save(@RequestBody OpenApiModel<ProjectPersonPlanRel> model);

    /**
     * 修改人员通行方案关系
     *
     * @param model 人员通行方案关系
     * @return R 返回修改后的人员通行方案关系
     */
    @PutMapping("/v1/meta/person-plan-rel")
    R<ProjectPersonPlanRel> update(@RequestBody OpenApiModel<ProjectPersonPlanRel> model);

    /**
     * 通过id删除人员通行方案关系
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要删除的主键id
     * @return R 返回删除结果
     */
    @DeleteMapping("/v1/meta/person-plan-rel/{id}")
    R<Boolean> delete(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);
}