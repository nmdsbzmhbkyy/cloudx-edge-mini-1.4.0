package com.aurine.cloudx.estate.feign;

import com.aurine.cloudx.common.core.constant.ServiceNameConstants;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(contextId = "remoteDockModuleService", value = ServiceNameConstants.ESTATE_SERVICE)
public interface RemoteDockModuleService {


     @GetMapping("/projectDockModuleConf/isWr20/{projectId}")
     R<Boolean> isWr20(@PathVariable("projectId") Integer projectId);

}
