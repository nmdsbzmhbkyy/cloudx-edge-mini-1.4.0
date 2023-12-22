package com.aurine.cloudx.open.origin.feign;

import com.pig4cloud.pigx.admin.api.dto.UserDTO;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.admin.api.vo.UserDeptVo;
import com.pig4cloud.pigx.admin.api.vo.UserVO;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * (remoteUmsUserService)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/12/9 11:17
 */
@FeignClient(contextId = "openRemoteUmsUserService", value = "ums-biz")
public interface RemoteUmsUserService {
    @GetMapping({"/user/{id}"})
    R<UserVO> user(@PathVariable("id") Integer userId);

    @GetMapping({"/user/details/{username}"})
    R<SysUser> user(@PathVariable("username") String username);

    @PutMapping({"/user/edit"})
    R<Boolean> updateUserInfo(@Valid @RequestBody UserDTO user);

    @GetMapping("/user/getUserDeptRole")
    R<List<UserDeptVo>> getUserDeptRole();

    @GetMapping("/user/getNewPhoneQRCode/{phone}")
     R<Boolean> getNewPhoneQRCode(@PathVariable("phone") String phone);


    @PutMapping("/user/checkNewPhoneQRCode/{phone}")
    R<Boolean> getNewPhoneQRCode(@PathVariable("phone") String phone, @RequestParam("code") String code);


}
