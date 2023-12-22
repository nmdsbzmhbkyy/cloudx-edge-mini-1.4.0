package com.aurine.cloudx.open.api.feign;

import com.aurine.cloudx.open.common.entity.vo.SysDeptVo;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 系统部门
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@FeignClient(contextId = "remoteOpenSysDeptService", value = "cloudx-open-biz")
public interface RemoteOpenSysDeptService {

    /**
     * 通过项目查询第一个系统部门
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @return R 返回第一个系统部门
     */
    @GetMapping("/v1/open/sys-dept/get-first-by-project")
    R<SysDeptVo> getFirstByProject(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId);
}
