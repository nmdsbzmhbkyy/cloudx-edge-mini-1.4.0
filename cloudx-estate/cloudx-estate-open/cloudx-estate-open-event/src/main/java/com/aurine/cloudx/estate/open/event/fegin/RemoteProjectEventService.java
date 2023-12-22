package com.aurine.cloudx.estate.open.event.fegin;

import com.aurine.cloudx.estate.entity.ProjectEntranceEvent;
import com.aurine.cloudx.estate.open.event.bean.ProjectEventSearchConditionPage;
import com.aurine.cloudx.estate.vo.ProjectEventVo;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;


@FeignClient(contextId = "remoteProjectEventService", value = "cloudx-estate-biz")
public interface RemoteProjectEventService {


    @GetMapping("/event/page")
    R getProjectEventPage(@SpringQueryMap ProjectEventSearchConditionPage projectEventSearchCondition);


    @GetMapping("/event/{seq}")
    R getById(@PathVariable("seq") Long seq);

    @PostMapping
    R save(@RequestBody ProjectEventVo projectEventVo);


    @GetMapping("/event/num")
    R find();

    @PutMapping("/event")
    R updateById(@RequestBody ProjectEntranceEvent projectEntranceEvent);


}
