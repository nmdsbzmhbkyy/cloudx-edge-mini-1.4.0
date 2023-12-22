package com.aurine.cloudx.estate.feign;

import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(contextId = "remoteWebSocketService", value = "cloudx-estate-biz")
public interface RemoteWebSocketService {
     @GetMapping("/projectwebsocket/findNumByProjectId")
     R findNumByProjectId();

     @GetMapping("/projectwebsocket/transferSocket/{projectId}")
     R transferSocket(@PathVariable("projectId") String projectId);

}
