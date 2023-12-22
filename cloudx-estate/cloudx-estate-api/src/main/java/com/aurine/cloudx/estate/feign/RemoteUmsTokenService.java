package com.aurine.cloudx.estate.feign;

import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


/**
 * (remoteUmsTokenService)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/12/9 11:17
 */
@FeignClient(contextId = "remoteUmsTokenService", value = "ums-auth")
public interface RemoteUmsTokenService {
    @DeleteMapping({"/token/{token}"})
    R<Boolean> removeTokenById(@PathVariable("token") String token, @RequestHeader("from") String form);

    @GetMapping("/token/judgment/{token}")
     R<Boolean> judgment(@PathVariable("token") String token, @RequestHeader("from") String form);


}
