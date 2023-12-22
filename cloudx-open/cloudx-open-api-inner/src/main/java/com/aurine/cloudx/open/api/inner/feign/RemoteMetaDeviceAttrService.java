package com.aurine.cloudx.open.api.inner.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.origin.entity.ProjectDeviceAttr;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 设备拓展属性管理
 *
 * @author : zouyu
 * @date : 2023-05-04 14:00:55
 */

@FeignClient(contextId = "remoteMetaDeviceAttr", value = "cloudx-open-biz")
public interface RemoteMetaDeviceAttrService {

    /**
     * 新增设备拓展属性
     *
     * @param model 设备拓展属性
     * @return R 返回新增后的设备拓展属性
     */
    @PostMapping("/v1/meta/device-attr")
    R<ProjectDeviceAttr> save(@RequestBody OpenApiModel<ProjectDeviceAttr> model);

    /**
     * 修改设备拓展属性
     *
     * @param model 设备拓展属性
     * @return R 返回修改后的设备拓展属性
     */
    @PutMapping("/v1/meta/device-attr")
    R<ProjectDeviceAttr> update(@RequestBody OpenApiModel<ProjectDeviceAttr> model);

    /**
     * 通过id删除设备拓展属性
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要删除的主键id
     * @return R 返回删除结果
     */
    @DeleteMapping("/v1/meta/device-attr/{id}")
    R<Boolean> delete(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);
}