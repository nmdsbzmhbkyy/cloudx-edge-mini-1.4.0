package com.aurine.cloudx.estate.feign;

import com.aurine.cloudx.common.core.constant.ServiceNameConstants;
import com.aurine.cloudx.estate.dto.ProjectDevicePassQRDTO;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * (RemoteDeviceInfo)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/12/9 8:55
 */
@FeignClient(contextId = "remoteDevicePassService", value = ServiceNameConstants.ESTATE_SERVICE)
public interface RemoteDevicePassService {


    /**
     * 获取二维码
     *
     * @param qrDto DTO对象
     * @return
     */
    @PostMapping("/projectDevicePass/qr-code")
    R getQRCode(ProjectDevicePassQRDTO qrDto);
}

