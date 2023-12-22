package com.aurine.cloudx.wjy.feign;

import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 路由测试
 */
@FeignClient(contextId = "remoteHelloService2", value = "cloudx-wjy-biz")
public interface RemoteHelloService {

    @RequestMapping("/hello/test")
    public R hello(@RequestParam("name") String name);

}
