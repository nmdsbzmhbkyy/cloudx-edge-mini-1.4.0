package com.aurine.cloudx.open.origin.feign;

import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(contextId = "openRemotePersonPlanRelService", value = "cloudx-estate-biz")
public interface RemotePersonPlanRelService {
    @GetMapping("/projectpersonplanrel/{seq}")
     R getById(@PathVariable("seq") Integer seq);
}
