package com.aurine.cloudx.open.api.inner.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.origin.entity.ProjectDeviceRegion;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 项目区域管理
 *
 * @author xull@aurine.cn
 * @date 2020-05-25 08:50:24
 */

@FeignClient(contextId = "remoteMetaProjectRegion", value = "cloudx-open-biz")
public interface RemoteMetaProjectRegionService {

    /**
     * 新增项目区域
     *
     * @param model 项目区域
     * @return R 返回新增后的项目区域
     */
    @PostMapping("/v1/meta/project-region")
    R<ProjectDeviceRegion> save(@RequestBody OpenApiModel<ProjectDeviceRegion> model);

    /**
     * 修改项目区域
     *
     * @param model 项目区域
     * @return R 返回修改后的项目区域
     */
    @PutMapping("/v1/meta/project-region")
    R<ProjectDeviceRegion> update(@RequestBody OpenApiModel<ProjectDeviceRegion> model);

    /**
     * 通过id删除项目区域
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要删除的主键id
     * @return R 返回删除结果
     */
    @DeleteMapping("/v1/meta/project-region/{id}")
    R<Boolean> delete(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);
}