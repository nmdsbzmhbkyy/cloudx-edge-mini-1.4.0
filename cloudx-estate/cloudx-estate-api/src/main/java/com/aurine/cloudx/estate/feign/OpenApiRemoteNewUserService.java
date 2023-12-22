package com.aurine.cloudx.estate.feign;

import com.aurine.cloudx.estate.constant.ServiceNameConstant;
import com.pig4cloud.pigx.admin.api.dto.CxUserDTO;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: wrm
 * @Date: 2022/06/21 10:32
 * @Package: com.aurine.openv2.api.feign
 * @Version: 1.0
 * @Remarks:
 **/
@Api(hidden = true)
@RequestMapping("/newUser")
@FeignClient(contextId = "openApiRemoteNewUserService", value = ServiceNameConstant.UPMS_SERVICE)
public interface OpenApiRemoteNewUserService {
	/**
	 * 保存用户权限
	 *
	 * @param var1
	 * @return
	 */
	@PostMapping({"/inner/saveUserRoleByStaff"})
	R<Integer> innerSaveUserRoleByStaff(@RequestBody CxUserDTO var1, @RequestHeader(SecurityConstants.FROM) String from);


	/**
	 * 根据手机号查询用户信息
	 *
	 * @param phone 手机号
	 * @return
	 */
	@GetMapping("/inner/details/{phone}")
	R<SysUser> innerGetUserByPhone(@PathVariable("phone") String phone, @RequestHeader(SecurityConstants.FROM) String from);
}
