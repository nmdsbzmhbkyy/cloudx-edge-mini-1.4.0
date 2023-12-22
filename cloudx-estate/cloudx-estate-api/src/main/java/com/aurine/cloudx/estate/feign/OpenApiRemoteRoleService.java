package com.aurine.cloudx.estate.feign;

import com.aurine.cloudx.estate.constant.ServiceNameConstant;
import com.pig4cloud.pigx.admin.api.entity.SysRole;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @Author: wrm
 * @Date: 2022/06/23 10:51
 * @Package: com.aurine.cloudx.estate.feign
 * @Version: 1.0
 * @Remarks:
 **/
@Api(hidden = true)
@RequestMapping("/role")
@FeignClient(contextId = "openApiRemoteRoleService", value = ServiceNameConstant.UPMS_SERVICE)
public interface OpenApiRemoteRoleService {
    @GetMapping({"/inner/dept/{id}"})
    R<List<SysRole>> innerGetByDeptId(@PathVariable("id") Integer id, @RequestHeader(SecurityConstants.FROM) String from);
}
