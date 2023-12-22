package com.aurine.cloudx.estate.open.event.fegin;

import com.aurine.cloudx.estate.entity.ProjectParkEntranceHis;
import com.aurine.cloudx.estate.open.event.bean.ProjectParkEntranceHisVoPage;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


@FeignClient(contextId = "remoteProjectParkEntranceHisService", value = "cloudx-estate-biz")
public interface RemoteProjectParkEntranceHisService {

    @GetMapping("/parkEntranceHis/page")
    R getParkEntranceHisPage(ProjectParkEntranceHisVoPage projectParkEntranceHisVo);


    @GetMapping("/parkEntranceHis/{parkOrderNo}")
    R getById(@PathVariable("parkOrderNo") String parkOrderNo);


    @PostMapping("/parkEntranceHis")
    R save(@RequestBody ProjectParkEntranceHis po);


    @PutMapping("/parkEntranceHis")
    R updateById(@RequestBody ProjectParkEntranceHis projectParkEntranceHis);

}
