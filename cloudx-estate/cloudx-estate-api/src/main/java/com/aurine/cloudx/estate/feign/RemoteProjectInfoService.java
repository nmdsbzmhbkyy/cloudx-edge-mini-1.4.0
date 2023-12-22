package com.aurine.cloudx.estate.feign;

import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(contextId = "remoteProjectInfoService", value = "cloudx-estate-biz")
public interface RemoteProjectInfoService {

    @GetMapping("/projectInfo/getLadderDeviceVersion/{projectId}")
    public R getLadderDeviceVersion(@PathVariable("projectId") Integer projectId);
    @PostMapping("/projectInfo/getProjectId")
    public R<Integer> getProjectId();

    @PostMapping("/projectInfo/getProjectUUID/{projectId}")
    R getProjectUUID(@PathVariable("projectId") Integer projectId, @RequestHeader(SecurityConstants.FROM) String from);
}
