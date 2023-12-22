package com.aurine.cloudx.open.origin.feign;

import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


/**
 * <p>
 *
 * </p>
 * @ClassName: RemoteCarRegisterConfService
 * @author: 王良俊 <>
 * @date:  2021年01月13日 上午11:02:41
 * @Copyright:
*/
@FeignClient(contextId = "openRemoteCarRegisterService", value = "cloudx-estate-biz")
public interface RemoteCarRegisterService {
    /**
     * 注销车辆登记
     *
     * @param registerId id
     * @return R
     */
    @GetMapping("/projectParCarRegister/cancelCarRegister/{registerId}")
    R cancelCarRegister(@PathVariable("registerId") String registerId);

}
