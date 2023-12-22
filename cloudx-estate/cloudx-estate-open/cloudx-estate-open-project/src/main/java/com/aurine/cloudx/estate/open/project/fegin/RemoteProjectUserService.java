package com.aurine.cloudx.estate.open.project.fegin;

import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.common.core.constant.ServiceNameConstants;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@FeignClient(contextId = "remoteProjectUserService", value = ServiceNameConstants.UPMS_SERVICE)
public interface RemoteProjectUserService {

    /**
     * 获取用户部门角色信息
     *
     * @return
     */
    @GetMapping("/user/getUserDeptRole")
    public R getUserDeptRole();
}
