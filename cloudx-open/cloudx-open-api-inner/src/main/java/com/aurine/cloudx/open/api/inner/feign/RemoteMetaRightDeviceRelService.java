package com.aurine.cloudx.open.api.inner.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.origin.entity.ProjectRightDevice;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 权限设备关系管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@FeignClient(contextId = "remoteMetaRightDeviceRel", value = "cloudx-open-biz")
public interface RemoteMetaRightDeviceRelService {

    /**
     * 新增权限设备关系
     *
     * @param model 权限设备关系
     * @return R 返回新增后的权限设备关系
     */
    @PostMapping("/v1/meta/right-device-rel")
    R<ProjectRightDevice> save(@RequestBody OpenApiModel<ProjectRightDevice> model);

    /**
     * 修改权限设备关系
     *
     * @param model 权限设备关系
     * @return R 返回修改后的权限设备关系
     */
    @PutMapping("/v1/meta/right-device-rel")
    R<ProjectRightDevice> update(@RequestBody OpenApiModel<ProjectRightDevice> model);

    /**
     * 通过id删除权限设备关系
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要删除的主键id
     * @return R 返回删除结果
     */
    @DeleteMapping("/v1/meta/right-device-rel/{id}")
    R<Boolean> delete(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);
}