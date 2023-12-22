package com.aurine.cloudx.estate.open.device.fegin;

import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(contextId = "remoteProjectRightDeviceService", value = "cloudx-estate-biz")
public interface RemoteProjectRightDeviceService {

    @GetMapping("/projectRightDevice/reDownloadCertByType/{deviceId}/{certType}")
    R reDownloadCertByType(@PathVariable("deviceId") String deviceId, @PathVariable("certType") String certType);

    @GetMapping("/projectRightDevice/clearCertByType/{deviceId}/{certType}")
    R clearCertByType(@PathVariable("deviceId") String deviceId, @PathVariable("certType") String certType);

    @PostMapping("/projectRightDevice/resentFailCert")
    R resendFailCert(@RequestBody List<String> deviceIdList);

    @PostMapping("/projectRightDevice/resendBatch")
    R resendBatch(@RequestBody List<String> rightDeviceIdList);
}
