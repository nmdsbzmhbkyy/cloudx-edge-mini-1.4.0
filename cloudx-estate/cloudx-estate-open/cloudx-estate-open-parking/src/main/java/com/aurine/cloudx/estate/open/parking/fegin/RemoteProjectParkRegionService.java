package com.aurine.cloudx.estate.open.parking.fegin;


import com.aurine.cloudx.estate.entity.ProjectParkRegion;
import com.aurine.cloudx.estate.open.parking.bean.ProjectParkRegionSeachConditionVoPage;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

@FeignClient(contextId = "remoteProjectParkRegionService", value = "cloudx-estate-biz")
public interface RemoteProjectParkRegionService {

    @GetMapping("/projectParkRegion/page")
    R getProjectParkRegionPage(@SpringQueryMap ProjectParkRegionSeachConditionVoPage page);

    @PostMapping("/projectParkRegion")
    R save(@RequestBody ProjectParkRegion projectParkRegion);


    @PutMapping("/projectParkRegion")
    R updateById(@RequestBody ProjectParkRegion projectParkRegion);


    @DeleteMapping("/projectParkRegion/{parkRegionId}")
    R removeById(@PathVariable("parkRegionId") String parkRegionId);
}
