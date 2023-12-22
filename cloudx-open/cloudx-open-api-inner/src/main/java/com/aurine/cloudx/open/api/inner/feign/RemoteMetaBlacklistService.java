package com.aurine.cloudx.open.api.inner.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.origin.entity.ProjectBlacklist;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 黑名单管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@FeignClient(contextId = "remoteMetaBlacklist", value = "cloudx-open-biz")
public interface RemoteMetaBlacklistService {

    /**
     * 新增黑名单
     *
     * @param model 黑名单
     * @return R 返回新增后的黑名单
     */
    @PostMapping("/v1/meta/blacklist")
    R<ProjectBlacklist> save(@RequestBody OpenApiModel<ProjectBlacklist> model);

//    /**
//     * 修改黑名单
//     *
//     * @param model 黑名单
//     * @return R 返回修改后的黑名单
//     */
//    @PutMapping("/v1/meta/blacklist")
//    R<ProjectBlacklist> update(@RequestBody OpenApiModel<ProjectBlacklist> model);

    /**
     * 通过id删除黑名单
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要删除的主键id
     * @return R 返回删除结果
     */
    @DeleteMapping("/v1/meta/blacklist/{id}")
    R<Boolean> delete(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);
}