package com.aurine.cloudx.open.api.inner.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.origin.entity.ProjectParkingInfo;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 车场信息管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@FeignClient(contextId = "remoteMetaParkingInfo", value = "cloudx-open-biz")
public interface RemoteMetaParkingInfoService {

    /**
     * 新增车场信息
     *
     * @param model 车场信息
     * @return R 返回新增后的车场信息
     */
    @PostMapping("/v1/meta/parking-info")
    R<ProjectParkingInfo> save(@RequestBody OpenApiModel<ProjectParkingInfo> model);

    /**
     * 修改车场信息
     *
     * @param model 车场信息
     * @return R 返回修改后的车场信息
     */
    @PutMapping("/v1/meta/parking-info")
    R<ProjectParkingInfo> update(@RequestBody OpenApiModel<ProjectParkingInfo> model);

    /**
     * 通过id删除车场信息
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要删除的主键id
     * @return R 返回删除结果
     */
    @DeleteMapping("/v1/meta/parking-info/{id}")
    R<Boolean> delete(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);
}