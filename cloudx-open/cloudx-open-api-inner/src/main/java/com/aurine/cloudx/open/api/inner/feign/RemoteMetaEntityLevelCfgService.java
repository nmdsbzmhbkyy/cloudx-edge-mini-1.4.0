package com.aurine.cloudx.open.api.inner.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.origin.entity.ProjectEntityLevelCfg;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 组团配置管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@FeignClient(contextId = "remoteMetaEntityLevelCfg", value = "cloudx-open-biz")
public interface RemoteMetaEntityLevelCfgService {

    /**
     * 新增组团配置
     * 注：组团配置可能没有新增功能，该接口只是统一定义后的结果
     *
     * @param model 组团配置
     * @return R 返回新增后的组团配置
     */
    @PostMapping("/v1/meta/entity-level-cfg")
    R<ProjectEntityLevelCfg> save(@RequestBody OpenApiModel<ProjectEntityLevelCfg> model);

    /**
     * 修改组团配置
     *
     * @param model 组团配置
     * @return R 返回修改后的组团配置
     */
    @PutMapping("/v1/meta/entity-level-cfg")
    R<ProjectEntityLevelCfg> update(@RequestBody OpenApiModel<ProjectEntityLevelCfg> model);

    /**
     * 通过id删除组团配置
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要删除的主键id
     * @return R 返回删除结果
     */
    @DeleteMapping("/v1/meta/entity-level-cfg/{id}")
    R<Boolean> delete(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);
}