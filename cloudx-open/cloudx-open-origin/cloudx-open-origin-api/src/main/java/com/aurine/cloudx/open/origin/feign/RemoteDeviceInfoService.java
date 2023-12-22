package com.aurine.cloudx.open.origin.feign;

import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * (RemoteDeviceInfo)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/12/9 8:55
 */
@FeignClient(contextId = "openRemoteDeviceInfoService", value = "cloudx-estate-biz")
public interface RemoteDeviceInfoService {

    @GetMapping("/projectDeviceInfo/open/{id}")
    R open(@PathVariable("id") String id);

    /**
     * 开门，带开门者信息
     * @param id
     * @param personType
     * @param personId
     * @return
     */
    @GetMapping("/projectDeviceInfo/open-by-person/{id}/{personType}/{personId}")
    R open(@PathVariable("id") String id, @PathVariable("personType") String personType, @PathVariable("personId") String personId);
}

