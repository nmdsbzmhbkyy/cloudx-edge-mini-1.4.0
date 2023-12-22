package com.aurine.cloudx.open.api.inner.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.origin.entity.ProjectCardHis;
import com.aurine.cloudx.open.origin.entity.ProjectConfig;
import com.aurine.cloudx.open.origin.entity.ProjectDeviceRegion;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 项目参数设置
 *
 * @author : zy
 * @date : 2022-11-24 09:50:33
 */
@FeignClient(contextId = "remoteMetaProjectConfig", value = "cloudx-open-biz")
public interface RemoteMetaProjectConfigService {

    /**
     * 项目参数设置
     *
     * @param model 项目参数设置
     * @return R 返回新增后的项目参数设置
     */
    @PostMapping("/v1/meta/config")
    R<ProjectConfig> save(@RequestBody OpenApiModel<ProjectCardHis> model);

    /**
     * 修改项目参数设置
     *
     * @param model 项目参数设置
     * @return R 返回修改后的项目参数设置
     */
    @PutMapping("/v1/meta/config")
    R<ProjectConfig> update(@RequestBody OpenApiModel<ProjectDeviceRegion> model);

    /**
     * 通过id删除项目参数设置
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要删除的主键id
     * @return R 返回删除结果
     */
    @DeleteMapping("/v1/meta/config/{id}")
    R<Boolean> delete(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);
}