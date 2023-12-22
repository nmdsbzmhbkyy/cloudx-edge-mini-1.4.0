package com.aurine.cloudx.estate.open.parking.fegin;

import com.aurine.cloudx.estate.open.parking.bean.ProjectParkingPlaceManageSearchConditionVoPage;
import com.aurine.cloudx.estate.vo.ProjectParkingPlaceManageVo;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


@FeignClient(contextId = "remoteProjectParkingPlaceManageService", value = "cloudx-estate-biz")
public interface RemoteProjectParkingPlaceManageService {


    @GetMapping("/page")
    R getProjectParkingPlacePage(ProjectParkingPlaceManageSearchConditionVoPage searchConditionVo);


    @GetMapping("/{id}")
    R getById(@PathVariable("id") String id);


    @GetMapping("/getParkRelNum")
    R getParkRelNum();

    @GetMapping("/more/{id}")
    R getMoreInfoById(@PathVariable("id") String id);

    @PostMapping
    R save(@RequestBody ProjectParkingPlaceManageVo projectParkingPlaceManageVo);

    @PutMapping
    R updateById(@RequestBody ProjectParkingPlaceManageVo projectParkingPlaceManageVo);

    @DeleteMapping("/{id}")
    R removeById(@PathVariable("id") String id);

    @GetMapping("/allocationPersonPublicParkingPlace/{parkId}/{personId}/{personName}")
    R removeById(@PathVariable("parkId") String parkId, @PathVariable("personId") String personId, @PathVariable("personName") String personName);


}
