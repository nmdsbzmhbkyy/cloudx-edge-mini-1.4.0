package com.aurine.cloudx.open.origin.feign;

import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * (remoteParkBillingRuleService)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/12/9 11:17
 */
@FeignClient(contextId = "openRemoteParkBillingRuleService", value = "cloudx-estate-biz")
public interface RemoteParkBillingRuleService {
    @GetMapping("/projectParkingBillRule/listUseable/{parkId}")
    public R listUseable(@PathVariable("parkId") String parkId);
}
