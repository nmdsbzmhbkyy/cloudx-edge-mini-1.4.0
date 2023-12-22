package com.aurine.cloudx.open.api.inner.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.origin.entity.ProjectParkRegion;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 车位区域管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@FeignClient(contextId = "remoteMetaParkRegion", value = "cloudx-open-biz")
public interface RemoteMetaParkRegionService {

    /**
     * 新增车位区域
     *
     * @param model 车位区域
     * @return R 返回新增后的车位区域
     */
    @PostMapping("/v1/meta/park-region")
    R<ProjectParkRegion> save(@RequestBody OpenApiModel<ProjectParkRegion> model);

    /**
     * 修改车位区域
     *
     * @param model 车位区域
     * @return R 返回修改后的车位区域
     */
    @PutMapping("/v1/meta/park-region")
    R<ProjectParkRegion> update(@RequestBody OpenApiModel<ProjectParkRegion> model);

    /**
     * 通过id删除车位区域
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要删除的主键id
     * @return R 返回删除结果
     */
    @DeleteMapping("/v1/meta/park-region/{id}")
    R<Boolean> delete(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);
}