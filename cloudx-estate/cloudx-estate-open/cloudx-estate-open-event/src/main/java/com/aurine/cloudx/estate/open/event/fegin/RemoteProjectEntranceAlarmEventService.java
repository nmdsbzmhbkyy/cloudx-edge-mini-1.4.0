package com.aurine.cloudx.estate.open.event.fegin;

import com.aurine.cloudx.estate.open.event.bean.ProjectEntranceAlarmEventVoPage;
import com.aurine.cloudx.estate.vo.ProjectEntranceAlarmEventVo;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

@FeignClient(contextId = "remoteProjectEntranceAlarmEventService", value = "cloudx-estate-biz")
public interface RemoteProjectEntranceAlarmEventService {


    @GetMapping("/projectentrancealarmevent/page")
    R getProjectEntranceAlarmEventPage(@SpringQueryMap ProjectEntranceAlarmEventVoPage page);

    @GetMapping("/projectentrancealarmevent/{eventId}")
    R getById(@PathVariable("eventId") String eventId);

    @PostMapping("/projectentrancealarmevent")
    R save(@RequestBody ProjectEntranceAlarmEventVo vo);


    @GetMapping("/projectentrancealarmevent")
    R getNum();

    @GetMapping("/projectentrancealarmevent/countAlarmNum")
    R getAllNum();


    @PutMapping("/projectentrancealarmevent/confirm")
    R confirmByStatus(@RequestBody ProjectEntranceAlarmEventVo vo);

    @PutMapping("/projectentrancealarmevent/modify")
    R modifyByStatus(@RequestBody ProjectEntranceAlarmEventVo vo);

    @PutMapping("/projectentrancealarmevent/batchModify")
    R batchModifyStatus(@RequestBody ProjectEntranceAlarmEventVo vo);

    @PutMapping("/projectentrancealarmevent/allHandle")
    R allHandle(@RequestBody ProjectEntranceAlarmEventVo vo);


    @GetMapping("/projectentrancealarmevent/countByMonth/{date}")
    R countByMonth(@PathVariable("date") String date);

    @GetMapping("/projectentrancealarmevent/countByMonthOff/{date}")
    R countByMonthOff(@PathVariable("date") String date);

    @GetMapping("/projectentrancealarmevent/countByOff")
    R countByOff();
}
