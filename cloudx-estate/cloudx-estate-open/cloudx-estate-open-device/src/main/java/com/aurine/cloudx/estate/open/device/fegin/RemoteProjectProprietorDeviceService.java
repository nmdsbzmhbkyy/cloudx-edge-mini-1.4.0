package com.aurine.cloudx.estate.open.device.fegin;

import com.aurine.cloudx.estate.open.device.bean.ProjectProprietorDeviceSearchConditionPage;
import com.aurine.cloudx.estate.vo.ProjectProprietorDeviceVo;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@FeignClient(contextId = "remoteProjectProprietorDeviceService", value = "cloudx-estate-biz")
public interface RemoteProjectProprietorDeviceService {


    @GetMapping("/serviceProprietorDevice/page")
    R getProjectPersonDevicePage(ProjectProprietorDeviceSearchConditionPage searchCondition);


    @GetMapping("/serviceProprietorDevice/{personId}")
    R getById(@PathVariable("personId") String personId);

    @GetMapping("/serviceProprietorDevice/listDevice/{personId}/{planId}")
    R getDeviceById(@PathVariable("personId") String personId, @PathVariable("planId") String planId);

    @PostMapping("/serviceProprietorDevice")
    R save(@Valid @RequestBody ProjectProprietorDeviceVo projectProprietorDeviceVo);


    @PutMapping("/serviceProprietorDevice")
    R updateById(@Valid @RequestBody ProjectProprietorDeviceVo projectProprietorDeviceVo);


    @GetMapping("/serviceProprietorDevice/enable/{personId}")
    R enablePassRight(@PathVariable("personId") String personId);

    @GetMapping("/serviceProprietorDevice/disable/{personId}")
    R disablePassRight(@PathVariable("personId") String personId);

}
