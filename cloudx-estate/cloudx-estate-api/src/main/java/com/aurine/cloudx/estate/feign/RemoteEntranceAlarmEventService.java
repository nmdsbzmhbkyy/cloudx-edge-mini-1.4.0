package com.aurine.cloudx.estate.feign;

import com.aurine.cloudx.estate.entity.SysEventExpertPlanConf;
import com.aurine.cloudx.estate.vo.ProjectEntranceAlarmEventVo;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.aurine.cloudx.common.core.constant.ServiceNameConstants.ESTATE_SERVICE;

/**
 * @version 1.0
 * @author： 林功鑫
 * @date： 2021-08-11 16:23
 */
@FeignClient(contextId = "RemoteEntranceAlarmEventService", value = ESTATE_SERVICE)
public interface RemoteEntranceAlarmEventService {
    /**
     * 获取预案信息
     *
     * @param eventTypeId
     * @return
     */
    @GetMapping("/sysExpertPlan/planInfoList")
    R<List<SysEventExpertPlanConf>> getPlanInfoList(@RequestParam(value = "eventTypeId") String eventTypeId);

    /**
     * 获取平面图列表
     * @return
     */
    @GetMapping("/baseFloorPlan/findList")
    R findPage(@SpringQueryMap Map<String,Object> map);

    /**
     * 分页查询警情记录
     * @param map
     * @return
     */
    @GetMapping("/projectentrancealarmevent/appPage")
    R getProjectEntranceAlarmEventPage(@SpringQueryMap Map<String,Object> map);

    /**
     * 通过id查询报警事件
     * @param eventId id
     * @return R
     */
    @GetMapping("/projectentrancealarmevent/{eventId}")
    R<ProjectEntranceAlarmEventVo> getById(@PathVariable("eventId" ) String eventId);


    /**
     * 修改处理状态为处理中并储存开始处理时间
     * @param vo
     * @return
     */
    @PutMapping("/projectentrancealarmevent/confirm")
    R<Boolean> setStatus(@RequestBody ProjectEntranceAlarmEventVo vo);

    /**
     * 修改处理状态为已处理并储存事件信息
     * @param vo
     * @return
     */
    @PutMapping("/projectentrancealarmevent/modify")
    R<Boolean> putStatus(@RequestBody ProjectEntranceAlarmEventVo vo);

    /**
     * 查询关联的设备
     * @param deviceId
     * @return
     */
    @GetMapping("/projectDeviceInfo/getMonitoring")
    R getMonitoring(@RequestParam(value = "deviceId")String deviceId);

    /**
     * 获取视频直播流的地址
     *
     * @param deviceId
     * @return
     */
    @GetMapping("/projectDeviceInfo/getLiveUrl/{deviceId}")
    R<String> getLiveUrl(@PathVariable(value = "deviceId") String deviceId);
}
