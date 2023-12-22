
package com.aurine.cloudx.estate.open.device.fegin;

import com.aurine.cloudx.estate.open.device.bean.ProjectStaffDeviceSearchConditionVoPage;
import com.aurine.cloudx.estate.vo.ProjectStaffDeviceVo;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(contextId = "remoteProjectStaffDeviceService", value = "cloudx-estate-biz")
public interface RemoteProjectStaffDeviceService {

    @GetMapping("/serviceStaffDevice/page")
    R getProjectStaffDevicePage(ProjectStaffDeviceSearchConditionVoPage searchConditionVo);


    @GetMapping("/serviceStaffDevice/{personId}")
    R getById(@PathVariable("personId") String personId);

    @GetMapping("/serviceStaffDevice/listDevice/{personId}/{planId}")
    R getDeviceById(@PathVariable("personId") String personId, @PathVariable("planId") String planId);

    @PostMapping("/serviceStaffDevice")
    R save(@Valid @RequestBody ProjectStaffDeviceVo projectStaffDevice);

    @GetMapping("/serviceStaffDevice/enable/{personId}")
    R enablePassRight(@PathVariable("personId") String personId);

    @GetMapping("/serviceStaffDevice/disable/{personId}")
    R disablePassRight(@PathVariable("personId") String personId);


}
