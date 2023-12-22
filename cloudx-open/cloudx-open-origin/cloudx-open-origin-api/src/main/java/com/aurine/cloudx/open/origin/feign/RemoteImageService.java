package com.aurine.cloudx.open.origin.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(contextId = "openRemoteImageService", value = "cloudx-face-service")
public interface RemoteImageService {
    @PostMapping("/images/upload-by-base64-with-water-mark")
    String base64ToImage(String base64);
}
