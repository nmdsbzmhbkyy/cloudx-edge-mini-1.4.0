

package com.aurine.cloudx.estate.open.device.fegin;

import com.aurine.cloudx.estate.open.device.bean.ProjectPassPlanPage;
import com.aurine.cloudx.estate.vo.ProjectPassPlanVo;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


@FeignClient(contextId = "remoteProjectPassPlanService", value = "cloudx-estate-biz")
public interface RemoteProjectPassPlanService {

    @GetMapping("/servicePassPlan/page")
    R getProjectPassPlanPage(ProjectPassPlanPage projectPassPlan);

    @GetMapping("/servicePassPlan/init")
    R initDefault();


    @GetMapping("/servicePassPlan/{id}")
    R getById(@PathVariable("id") String id);


    @PostMapping("/servicePassPlan")
    R save(@RequestBody ProjectPassPlanVo projectPassPlanVo);

    @PutMapping("/servicePassPlan")
    R updateById(@RequestBody ProjectPassPlanVo projectPassPlanVo);

    @DeleteMapping("/servicePassPlan/{id}")
    R removeById(@PathVariable("id") String id);


    @GetMapping("/servicePassPlan/listByType/{planObject}")
    R list(@PathVariable("planObject") String planObject);


    @GetMapping("/servicePassPlan/listDevice/{id}")
    R listDevice(@PathVariable("id") String id);

}
