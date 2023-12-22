package com.aurine.cloudx.open.api.inner.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.origin.entity.ProjectVisitorHis;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 来访记录管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@FeignClient(contextId = "remoteMetaVisitorHis", value = "cloudx-open-biz")
public interface RemoteMetaVisitorHisService {

    /**
     * 新增来访记录
     *
     * @param model 来访记录
     * @return R 返回新增后的来访记录
     */
    @PostMapping("/v1/meta/visitor-his")
    R<ProjectVisitorHis> save(@RequestBody OpenApiModel<ProjectVisitorHis> model);

    /**
     * 修改来访记录
     *
     * @param model 来访记录
     * @return R 返回修改后的来访记录
     */
    @PutMapping("/v1/meta/visitor-his")
    R<ProjectVisitorHis> update(@RequestBody OpenApiModel<ProjectVisitorHis> model);

    /**
     * 通过id删除来访记录
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要删除的主键id
     * @return R 返回删除结果
     */
    @DeleteMapping("/v1/meta/visitor-his/{id}")
    R<Boolean> delete(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);
}