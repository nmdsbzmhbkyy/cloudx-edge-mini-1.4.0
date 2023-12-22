package com.aurine.cloudx.open.origin.feign;

import com.aurine.cloudx.open.origin.vo.ProjectParCarRegisterVo;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * (remoteParCarRegisterService)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/12/9 11:17
 */
@FeignClient(contextId = "openRemoteParCarRegisterService", value = "cloudx-estate-biz")
public interface RemoteParCarRegisterService {
    @PostMapping("/projectParCarRegister")
    R register(@RequestBody ProjectParCarRegisterVo projectParCarRegister);
}
