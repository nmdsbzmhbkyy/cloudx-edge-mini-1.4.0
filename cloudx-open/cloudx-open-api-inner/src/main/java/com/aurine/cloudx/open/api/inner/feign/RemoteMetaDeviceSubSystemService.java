package com.aurine.cloudx.open.api.inner.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.origin.entity.ProjectDeviceSubsystem;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * open平台-设备子系统管理
 *
 * @author : linlx
 * @date : 2022 3 24 9:43
 */

@FeignClient(contextId = "remoteMetaDeviceSubSystem", value = "cloudx-open-biz")
public interface RemoteMetaDeviceSubSystemService {

    /**
     * 新增设备子系统
     *
     * @param model 设备子系统
     * @return R 返回新增后的设备子系统
     */
    @PostMapping("/v1/meta/device-sub-system")
    R<ProjectDeviceSubsystem> save(@RequestBody OpenApiModel<ProjectDeviceSubsystem> model);
}
