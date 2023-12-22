package com.aurine.cloudx.open.api.inner.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.origin.entity.ProjectParkCarType;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 车辆类型管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@FeignClient(contextId = "remoteMetaParkCarType", value = "cloudx-open-biz")
public interface RemoteMetaParkCarTypeService {

    /**
     * 新增车辆类型
     *
     * @param model 车辆类型
     * @return R 返回新增后的车辆类型
     */
    @PostMapping("/v1/meta/park-car-type")
    R<ProjectParkCarType> save(@RequestBody OpenApiModel<ProjectParkCarType> model);

    /**
     * 修改车辆类型
     *
     * @param model 车辆类型
     * @return R 返回修改后的车辆类型
     */
    @PutMapping("/v1/meta/park-car-type")
    R<ProjectParkCarType> update(@RequestBody OpenApiModel<ProjectParkCarType> model);

    /**
     * 通过id删除车辆类型
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要删除的主键id
     * @return R 返回删除结果
     */
    @DeleteMapping("/v1/meta/park-car-type/{id}")
    R<Boolean> delete(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);
}