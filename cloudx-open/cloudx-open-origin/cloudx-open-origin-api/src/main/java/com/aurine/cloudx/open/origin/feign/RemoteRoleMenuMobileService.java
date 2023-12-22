package com.aurine.cloudx.open.origin.feign;

import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(contextId = "openRemoteRoleMenuMobileService", value = "cloudx-estate-biz")
public interface RemoteRoleMenuMobileService {
    @GetMapping("/sys-role-menu-mobile/list/id/{roleId}")
    R listIdByRoleId(@PathVariable("roleId") Integer roleId);

    @GetMapping("/sys-role-menu-mobile/list/permission/{roleId}")
    R listPermissionByRoleId(@PathVariable("roleId") Integer roleId);

    @GetMapping("/sys-role-menu-mobile/tree/{roleId}")
    R treeRoleId(@PathVariable("roleId") Integer roleId);

    @GetMapping("/sys-role-menu-mobile/tree/{roleId}/{type}")
    R treeTypeRoleId(@PathVariable("roleId") Integer roleId, @PathVariable("type") Integer type);
}
