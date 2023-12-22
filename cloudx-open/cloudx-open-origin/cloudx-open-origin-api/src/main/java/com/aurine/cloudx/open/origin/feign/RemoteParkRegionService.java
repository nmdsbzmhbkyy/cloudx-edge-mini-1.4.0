package com.aurine.cloudx.open.origin.feign;

import com.aurine.cloudx.open.origin.entity.ProjectParkingPlace;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * (remoteParkBillingRuleService)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/12/9 11:17
 */
@FeignClient(contextId = "openRemoteParkRegionService", value = "cloudx-estate-biz")
public interface RemoteParkRegionService {
    @GetMapping("/projectParkRegion/listPersonAttrParkingRegionByRelType")
    R getList(@RequestParam("projectParkingPlace") ProjectParkingPlace projectParkingPlace);
}
