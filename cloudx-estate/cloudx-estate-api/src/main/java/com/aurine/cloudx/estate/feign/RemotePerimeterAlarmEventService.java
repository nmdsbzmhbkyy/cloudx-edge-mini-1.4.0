package com.aurine.cloudx.estate.feign;

import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(contextId = "remotePerimeterAlarmEventService", value = "cloudx-estate-biz")
public interface RemotePerimeterAlarmEventService {


    @GetMapping("/projectPerimeterAlarmEvent/page")
    R<Page<ProjectPerimeterAlarmEventVo>> findAll(@SpringQueryMap AppPerimeterAlarmEventFromVo appPerimeterAlarmEventFromVo);

    @DeleteMapping("projectPerimeterAlarmEvent/{eventId}")
    R deleteByEventId(@PathVariable("eventId") String eventId);

    @GetMapping("/getLiveUrl/{deviceId}")
    R<String> getLiveUrl(@PathVariable("deviceId") String deviceId);


    @GetMapping("/baseFloorPlan/findList")
    R findList(@SpringQueryMap AppFloorPicSearchConditionVo appFloorPicSearchConditionVo);

    @GetMapping("/projectDeviceInfo/getMonitoring")
    R getMonitoring(@RequestParam("deviceId") String deviceId);

    @GetMapping("/baseFloorPlan/getPicLocation")
    R<Page<ProjectFloorPicVo>> getPicLocation(@SpringQueryMap AppFloorPicSearchConditionVo appFloorPicSearchConditionVo);
}
