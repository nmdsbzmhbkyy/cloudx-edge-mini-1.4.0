package com.aurine.cloudx.estate.feign;

import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@FeignClient(contextId = "remoteImageService", value = "cloudx-face-service")
public interface RemoteImageService {
    @PostMapping("/images/upload-by-base64-with-water-mark")
    String base64ToImage(String base64);
}
