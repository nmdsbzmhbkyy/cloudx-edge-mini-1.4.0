package com.aurine.cloudx.estate.feign;

import com.aurine.cloudx.estate.entity.ProjectFaceResources;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * (RemoteFaceRecources)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/12/9 10:26
 */
@FeignClient(contextId = "remoteFaceResourcesService", value = "cloudx-estate-biz")
public interface RemoteFaceResourcesService {
    @PostMapping("/projectFaceResources/saveFaceByApp")
    public R saveFaceByApp(@RequestBody ProjectFaceResources projectFaceResources);
}
