

package com.aurine.cloudx.push.feign;

import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author lengleng
 * @date 2018/6/22
 */
//@FeignClient(contextId = "remoteTestService", value = "cloudx-test")
public interface RemoteTestService {
	
	@GetMapping("/test")
	String sendSmsCode();
}
