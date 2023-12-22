package com.aurine.cloudx.estate.open.parking.fegin;

import com.aurine.cloudx.estate.entity.ProjectCarPreRegister;
import com.aurine.cloudx.estate.open.parking.bean.CarPreRegisterSearchConditionPage;
import com.aurine.cloudx.estate.vo.ProjectCarPreRegisterAuditVo;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@FeignClient(contextId = "remoteProjectCarPreRegisterService", value = "cloudx-estate-biz")
public interface RemoteProjectCarPreRegisterService {


    @GetMapping("/page")
    R fetchList(CarPreRegisterSearchConditionPage searchCondition);


    @GetMapping("{preRegId}")
    R selectOne(@PathVariable("preRegId") String preRegId);

    @GetMapping("/getAuditInfo/{preRegId}")
    R getAuditInfo(@PathVariable("preRegId") String preRegId);

    @PostMapping("/rejectAudit")
    R rejectAudit(@RequestBody ProjectCarPreRegister carPreRegister);


    @GetMapping("/checkHasBeenApplied/{plateNumber}")
    R checkHasBeenApplied(@PathVariable("plateNumber") String plateNumber);


    @PostMapping("/passAudit")
    R passAudit(@RequestBody ProjectCarPreRegisterAuditVo preRegisterAuditVo);

    @PostMapping
    R insert(@RequestBody ProjectCarPreRegister projectCarPreRegister);

    @PutMapping
    R update(@RequestBody ProjectCarPreRegister projectCarPreRegister);

    @DeleteMapping
    R delete(@RequestParam("idList") List<Integer> idList);
}

