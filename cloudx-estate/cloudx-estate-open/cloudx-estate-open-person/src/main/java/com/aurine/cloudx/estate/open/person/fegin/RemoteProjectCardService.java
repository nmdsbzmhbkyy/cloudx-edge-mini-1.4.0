package com.aurine.cloudx.estate.open.person.fegin;

import com.aurine.cloudx.estate.entity.ProjectCard;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(contextId = "remoterProjectCardService", value = "cloudx-estate-biz")
public interface RemoteProjectCardService {

    @GetMapping("/projectCard/list/{personId}")
    public R listProjectCardByPersonId(@PathVariable("personId") String personId);

    @PostMapping("/projectCard")
    public R save(@RequestBody ProjectCard projectCard);

    @DeleteMapping("/projectCard/{cardId}")
    public R removeById(@PathVariable("cardId") String cardId);
}
