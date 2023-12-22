package com.aurine.cloudx.open.origin.feign;

import com.aurine.cloudx.open.origin.vo.ProjectProprietorDeviceVo;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(contextId = "openRemoteServiceProprietorDeviceService", value = "cloudx-estate-biz")
public interface RemoteServiceProprietorDeviceService {
    /**
     * 通过id查询人员设备权限
     *
     * @param personId personId
     * @return R
     */
    @GetMapping("/serviceProprietorDevice/{personId}")
    R<ProjectProprietorDeviceVo> getById(@PathVariable("personId") String personId);
}
