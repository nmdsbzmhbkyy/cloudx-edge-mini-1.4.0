package com.aurine.cloudx.estate.open.parking.fegin;

import com.aurine.cloudx.estate.entity.ProjectParkingPlace;
import com.aurine.cloudx.estate.open.parking.bean.ProjectParkingPlaceConditionVoPage;
import com.aurine.cloudx.estate.vo.ProjectParkingPlaceVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

@FeignClient(contextId = "remoteProjectParkingPlaceService", value = "cloudx-estate-biz")
public interface RemoteProjectParkingPlaceService {

    @GetMapping("/baseParkingPlace/page" )
    R getProjectParkingPlacePage(@SpringQueryMap ProjectParkingPlaceConditionVoPage page);


    @PostMapping("/baseParkingPlace")
    R save(@RequestBody ProjectParkingPlace projectParkingPlace);

    @PutMapping("/baseParkingPlace")
    R updateById(@RequestBody ProjectParkingPlace projectParkingPlace);

    @DeleteMapping("/baseParkingPlace/{id}")
    R removeById(@PathVariable("id") String id);
}
