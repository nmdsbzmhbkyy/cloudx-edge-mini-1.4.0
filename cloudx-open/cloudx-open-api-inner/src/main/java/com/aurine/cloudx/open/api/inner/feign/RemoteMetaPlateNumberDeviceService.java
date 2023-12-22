package com.aurine.cloudx.open.api.inner.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.origin.entity.ProjectPlateNumberDevice;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 设备车牌号下发情况管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@FeignClient(contextId = "remoteMetaPlateNumberDevice", value = "cloudx-open-biz")
public interface RemoteMetaPlateNumberDeviceService {

    /**
     * 新增设备车牌号下发情况
     *
     * @param model 设备车牌号下发情况
     * @return R 返回新增后的设备车牌号下发情况
     */
    @PostMapping("/v1/meta/plate-number-device")
    R<ProjectPlateNumberDevice> save(@RequestBody OpenApiModel<ProjectPlateNumberDevice> model);

    /**
     * 修改设备车牌号下发情况
     *
     * @param model 设备车牌号下发情况
     * @return R 返回修改后的设备车牌号下发情况
     */
    @PutMapping("/v1/meta/plate-number-device")
    R<ProjectPlateNumberDevice> update(@RequestBody OpenApiModel<ProjectPlateNumberDevice> model);

    /**
     * 通过id删除设备车牌号下发情况
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要删除的主键id
     * @return R 返回删除结果
     */
    @DeleteMapping("/v1/meta/plate-number-device/{id}")
    R<Boolean> delete(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);
}