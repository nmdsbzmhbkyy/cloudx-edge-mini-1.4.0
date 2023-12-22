package com.aurine.cloudx.estate.feign;

import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(contextId = "remotePersonPlanRelService", value = "cloudx-estate-biz")
public interface RemotePersonPlanRelService {
    @GetMapping("/projectpersonplanrel/{seq}")
     R getById(@PathVariable("seq") Integer seq);
}
