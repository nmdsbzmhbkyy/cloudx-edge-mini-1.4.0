package com.aurine.cloudx.estate.open.person.fegin;

import com.aurine.cloudx.estate.entity.ProjectFaceResources;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(contextId = "remoteProjectFaceResourcesService", value = "cloudx-estate-biz")
public interface RemoteProjectFaceResourcesService {

    @GetMapping("/projectFaceResources/list/{personId}")
    R listProjectFaceResourcesByPersonId(@PathVariable("personId") String personId);

    @PostMapping("/projectFaceResources")
    R save(@RequestBody ProjectFaceResources projectFaceResources);

    @DeleteMapping("/projectFaceResources/{faceId}")
    R removeById(@PathVariable("faceId") String faceId);
}
