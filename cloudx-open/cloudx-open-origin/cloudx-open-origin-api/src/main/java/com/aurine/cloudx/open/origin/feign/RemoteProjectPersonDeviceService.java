package com.aurine.cloudx.open.origin.feign;

import com.aurine.cloudx.open.origin.constant.enums.PersonTypeEnum;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * (RemoteFaceRecources)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/12/9 10:26
 */
@FeignClient(contextId = "openRemoteProjectPersonDeviceService", value = "cloudx-estate-biz")
public interface RemoteProjectPersonDeviceService {
    /**
     * 根据personId,重载该用户的权限，用于用户房屋归属变化等情况
     *
     * @param personId
     * @return
     */
    @GetMapping("/projectPersonDevice/refreshByPersonId")
    boolean refreshByPersonId(@RequestParam("personId") String personId, @RequestParam("personTypeEnum") PersonTypeEnum personTypeEnum);

}
