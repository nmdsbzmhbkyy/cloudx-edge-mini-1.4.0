package com.aurine.cloudx.estate.feign;

import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @ClassName RemoteVisitorHisService
 * @Description
 * @Author linlx
 * @Date 2022/7/7 17:45
 **/
@FeignClient(contextId = "remoteVisitorHisService", value = "cloudx-estate-biz")
public interface RemoteVisitorHisService {

    @GetMapping("/serviceVisitorHis/getByPlateNumber/{plateNumber}")
    R getByPlateNumber(@PathVariable("plateNumber")String plateNumber,@RequestHeader(SecurityConstants.FROM) String from);
}
