package com.aurine.cloudx.open.api.inner.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.origin.entity.ProjectPhysicalPassPolicy;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 物理策略管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@FeignClient(contextId = "remoteMetaPhysicalPassPolicy", value = "cloudx-open-biz")
public interface RemoteMetaPhysicalPassPolicyService {

    /**
     * 新增物理策略
     *
     * @param model 物理策略
     * @return R 返回新增后的物理策略
     */
    @PostMapping("/v1/meta/physical-pass-policy")
    R<ProjectPhysicalPassPolicy> save(@RequestBody OpenApiModel<ProjectPhysicalPassPolicy> model);

    /**
     * 修改物理策略
     *
     * @param model 物理策略
     * @return R 返回修改后的物理策略
     */
    @PutMapping("/v1/meta/physical-pass-policy")
    R<ProjectPhysicalPassPolicy> update(@RequestBody OpenApiModel<ProjectPhysicalPassPolicy> model);

    /**
     * 通过id删除物理策略
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要删除的主键id
     * @return R 返回删除结果
     */
    @DeleteMapping("/v1/meta/physical-pass-policy/{id}")
    R<Boolean> delete(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);
}