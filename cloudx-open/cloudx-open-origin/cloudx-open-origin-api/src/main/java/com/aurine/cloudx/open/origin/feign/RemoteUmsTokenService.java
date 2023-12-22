package com.aurine.cloudx.open.origin.feign;

import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;


/**
 * (remoteUmsTokenService)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/12/9 11:17
 */
@FeignClient(contextId = "openRemoteUmsTokenService", value = "ums-auth")
public interface RemoteUmsTokenService {
    @DeleteMapping({"/token/{token}"})
    R<Boolean> removeTokenById(@PathVariable("token") String token, @RequestHeader("from") String form);

    @GetMapping("/token/judgment/{token}")
     R<Boolean> judgment(@PathVariable("token") String token, @RequestHeader("from") String form);


}
