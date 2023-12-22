package com.aurine.cloudx.open.api.inner.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.origin.entity.ProjectEntranceEvent;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 人行事件管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@FeignClient(contextId = "remoteMetaPersonEntrance", value = "cloudx-open-biz")
public interface RemoteMetaPersonEntranceService {

    /**
     * 新增人行事件
     *
     * @param model 人行事件
     * @return R 返回新增后的人行事件
     */
    @PostMapping("/v1/meta/person-entrance")
    R<ProjectEntranceEvent> save(@RequestBody OpenApiModel<ProjectEntranceEvent> model);

    /**
     * 修改人行事件
     *
     * @param model 人行事件
     * @return R 返回修改后的人行事件
     */
    @PutMapping("/v1/meta/person-entrance")
    R<ProjectEntranceEvent> update(@RequestBody OpenApiModel<ProjectEntranceEvent> model);

    /**
     * 通过id删除人行事件
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要删除的主键id
     * @return R 返回删除结果
     */
    @DeleteMapping("/v1/meta/person-entrance/{id}")
    R<Boolean> delete(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);
}