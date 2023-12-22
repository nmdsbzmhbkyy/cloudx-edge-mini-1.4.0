package com.aurine.cloudx.estate.feign;

import com.pig4cloud.pigx.admin.api.dto.UserDTO;
import com.pig4cloud.pigx.admin.api.entity.SysDictItem;
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
@FeignClient(contextId = "remotePigxUserService", value = "pigx-upms-biz")
public interface RemotePigxUserService {


     @PutMapping("/user/updateByBiz/{phone}")
     R<Boolean> updateByBiz(@PathVariable("phone") String phone);

     @PutMapping({"/newUser/edit"})
     R<Boolean> updateUserInfo(@Valid @RequestBody UserDTO var1);

     @GetMapping({"/dict/type/{type}"})
     R<List<SysDictItem>> getDictByType(@PathVariable("type") String type);
}
