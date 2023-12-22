package com.aurine.cloudx.open.api.inner.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.origin.entity.ProjectDeviceParamInfo;
import com.aurine.cloudx.open.origin.entity.ProjectPersonLiftRel;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 设备参数
 *
 * @author : zy
 * @date : 2022-07-18 09:46:54
 */

@FeignClient(contextId = "remoteMetaDeviceParamInfo", value = "cloudx-open-biz")
public interface RemoteMetaDeviceParamInfoService {


    /**
     * 新增设备参数
     *
     * @param model 设备参数
     * @return R 返回新增后的设备参数
     */
    @PostMapping("/v1/meta/device-param-info")
     R<ProjectDeviceParamInfo> save(@RequestBody OpenApiModel<ProjectDeviceParamInfo> model);

    /**
     * 修改设备参数
     *
     * @param model 设备参数
     * @return R 返回修改后的设备参数
     */

    @PutMapping("/v1/meta/device-param-info")
     R<ProjectDeviceParamInfo> update( @RequestBody OpenApiModel<ProjectDeviceParamInfo> model);

    /**
     * 通过id删除设备参数
     *
     * @param id
     * @return
     */
    @DeleteMapping("/v1/meta/device-param-info/{id}")
    R<Boolean> delete(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);
}