package com.aurine.cloudx.open.origin.feign;

import com.pig4cloud.pigx.admin.api.dto.UserDTO;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * (remoteUmsUserService)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/12/9 11:17
 */
@FeignClient(contextId = "openRemotePigxUserService", value = "pigx-upms-biz")
public interface RemotePigxUserService {


     @PutMapping("/user/updateByBiz/{phone}")
     R<Boolean> updateByBiz(@PathVariable("phone") String phone);

     @PutMapping({"/newUser/edit"})
     R<Boolean> updateUserInfo(@Valid @RequestBody UserDTO var1);



}
