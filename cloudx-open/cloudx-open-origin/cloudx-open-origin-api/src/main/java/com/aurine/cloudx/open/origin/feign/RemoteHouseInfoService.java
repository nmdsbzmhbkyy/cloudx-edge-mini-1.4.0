package com.aurine.cloudx.open.origin.feign;

import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @author ： huangjj
 * @date ： 2021/5/13
 * @description： 房间管理接口
 */
@FeignClient(contextId = "openRemoteHouseInfoService", value = "cloudx-estate-biz")
public interface RemoteHouseInfoService {
    @GetMapping("/baseHouse/inner/list/{projectId}/{unitId}")
    public R innerListByUnit(@PathVariable(value = "projectId") Integer projectId,
                             @PathVariable(value = "unitId") String unitId,
                             @RequestHeader(SecurityConstants.FROM) String from);
    @GetMapping("/baseHouse/inner/info/{id}")
    public R innerInfoById(@PathVariable("id") String id,
                           @RequestHeader(SecurityConstants.FROM) String from);
}
