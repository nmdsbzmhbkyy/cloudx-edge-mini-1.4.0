package com.aurine.cloudx.open.origin.feign;

import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * (remoteParkBillingRuleService)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/12/9 11:17
 */
@FeignClient(contextId = "openRemoteParkingInfoService", value = "cloudx-estate-biz")
public interface RemoteParkingInfoService {
    @GetMapping("/baseParkingArea/list")
    R list();
}
