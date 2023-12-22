package com.aurine.cloudx.open.api.inner.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.origin.entity.ProjectEntryExitLane;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 出入口车道管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@FeignClient(contextId = "remoteMetaEntryExitLane", value = "cloudx-open-biz")
public interface RemoteMetaEntryExitLaneService {

    /**
     * 新增出入口车道
     *
     * @param model 出入口车道
     * @return R 返回新增后的出入口车道
     */
    @PostMapping("/v1/meta/entry-exit-lane")
    R<ProjectEntryExitLane> save(@RequestBody OpenApiModel<ProjectEntryExitLane> model);

    /**
     * 修改出入口车道
     *
     * @param model 出入口车道
     * @return R 返回修改后的出入口车道
     */
    @PutMapping("/v1/meta/entry-exit-lane")
    R<ProjectEntryExitLane> update(@RequestBody OpenApiModel<ProjectEntryExitLane> model);

    /**
     * 通过id删除出入口车道
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要删除的主键id
     * @return R 返回删除结果
     */
    @DeleteMapping("/v1/meta/entry-exit-lane/{id}")
    R<Boolean> delete(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);
}