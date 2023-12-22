package com.aurine.cloudx.estate.fegin;

import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(contextId = "remoteFaceService", value = "cloudx-face-service", path = "face")
public interface RemoteFaceService {

    @PostMapping("/uploadAndCheck/{type}")
    public R indexPost(@RequestParam("file") MultipartFile upload, @PathVariable("type") String type);

    @PostMapping("/checkFile")
    public R check(@RequestParam("file") MultipartFile upload);
}
