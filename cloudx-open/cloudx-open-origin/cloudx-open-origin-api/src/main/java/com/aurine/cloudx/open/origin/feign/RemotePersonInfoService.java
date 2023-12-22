package com.aurine.cloudx.open.origin.feign;

import com.aurine.cloudx.open.origin.entity.ProjectPersonInfo;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(contextId = "openRemotePersonInfoService", value = "cloudx-estate-biz")
public interface RemotePersonInfoService {
    @GetMapping("/basePersonInfo/get-owner")
    R<ProjectPersonInfo> getOwner();

    @GetMapping("/basePersonInfo/inner/{id}")
    R innerGetById(@PathVariable("id") String id,
                   @RequestHeader(SecurityConstants.FROM) String from);


}
