package com.aurine.cloudx.open.api.inner.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.origin.entity.ProjectParCarRegister;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 车辆登记管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@FeignClient(contextId = "remoteMetaParCarRegister", value = "cloudx-open-biz")
public interface RemoteMetaParCarRegisterService {

    /**
     * 新增车辆登记
     *
     * @param model 车辆登记
     * @return R 返回新增后的车辆登记
     */
    @PostMapping("/v1/meta/par-car-register")
    R<ProjectParCarRegister> save(@RequestBody OpenApiModel<ProjectParCarRegister> model);

    /**
     * 修改车辆登记
     *
     * @param model 车辆登记
     * @return R 返回修改后的车辆登记
     */
    @PutMapping("/v1/meta/par-car-register")
    R<ProjectParCarRegister> update(@RequestBody OpenApiModel<ProjectParCarRegister> model);

    /**
     * 通过id删除车辆登记
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要删除的主键id
     * @return R 返回删除结果
     */
    @DeleteMapping("/v1/meta/par-car-register/{id}")
    R<Boolean> delete(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);
}