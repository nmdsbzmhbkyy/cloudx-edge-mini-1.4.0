

package com.aurine.cloudx.estate.open.device.fegin;

import com.aurine.cloudx.estate.open.device.bean.ProjectPassPlanPage;
import com.aurine.cloudx.estate.vo.ProjectLiftPlanVo;
import com.aurine.cloudx.estate.vo.ProjectPassPlanVo;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


@FeignClient(contextId = "remoteProjectLiftPlanService", value = "cloudx-estate-biz")
public interface RemoteProjectLiftPlanService {

    @GetMapping("/serviceLiftPlan/page")
    R getProjectPassPlanPage(ProjectPassPlanPage projectPassPlan);

    @GetMapping("/serviceLiftPlan/init")
    R initDefault();


    @GetMapping("/serviceLiftPlan/{id}")
    R getById(@PathVariable("id") String id);


    @PostMapping("/serviceLiftPlan")
    R save(@RequestBody ProjectLiftPlanVo projectPassPlanVo);

    @PutMapping("/serviceLiftPlan")
    R updateById(@RequestBody ProjectLiftPlanVo projectPassPlanVo);

    @DeleteMapping("/serviceLiftPlan/{id}")
    R removeById(@PathVariable("id") String id);


    @GetMapping("/serviceLiftPlan/listByType/{planObject}")
    R list(@PathVariable("planObject") String planObject);


    @GetMapping("/serviceLiftPlan/listDevice/{id}")
    R listDevice(@PathVariable("id") String id);

}
