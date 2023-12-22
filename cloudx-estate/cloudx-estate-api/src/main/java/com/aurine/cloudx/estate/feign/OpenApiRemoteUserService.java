package com.aurine.cloudx.estate.feign;

import com.aurine.cloudx.estate.constant.ServiceNameConstant;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author: wrm
 * @Date: 2022/06/06 16:51
 * @Package: com.aurine.openv2.api.feign
 * @Version: 1.0
 * @Remarks:
 **/
@Api(hidden = true)
@RequestMapping("/user")
@FeignClient(contextId = "openApiRemoteUserService", value = ServiceNameConstant.UPMS_SERVICE)
public interface OpenApiRemoteUserService {

	/**
	 * 根据用户名查询用户信息
	 *
	 * @param username 用户名
	 * @param from 内部调用鉴权请求头
	 * @return
	 */
	@GetMapping("/inner/details/{username}")
	R<SysUser> innerGetUserByUserNmae(@PathVariable("username") String username, @RequestHeader(SecurityConstants.FROM) String from);

	/**
	 * 删除用户角色关系
	 * @param userId
	 * @param roleId
	 * @return
	 */
	@PutMapping("/inner/removeUserRole")
	R<Boolean> innerRemoveUserRole(@RequestParam("userId") Integer userId, @RequestParam("roleId") Integer roleId, @RequestHeader(SecurityConstants.FROM) String from);

}
