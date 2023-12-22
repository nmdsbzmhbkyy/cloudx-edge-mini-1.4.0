package com.aurine.cloudx.estate.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.service.*;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;

@Service
@Slf4j
public class ProjectWebSocketServiceImpl implements ProjectWebSocketService {




    @Resource
    private ProjectComplaintRecordService projectComplaintRecordService;
    @Resource
    private ProjectRepairRecordService projectRepairRecordService;
    @Resource
    private ProjectHousePersonRelService projectHousePersonRelService;
    @Resource
    private ProjectCarPreRegisterService projectCarPreRegisterService;
    @Resource
    private ProjectVisitorHisService projectVisitorHisService;
    @Resource
    private ProjectEntranceAlarmEventService projectEntranceAlarmEventService;
    @Resource
    private ProjectPerimeterAlarmEventService projectPerimeterAlarmEventService;


    @Override
    public R findNumByProjectId() {


        try {
            HashMap<String, Integer> map = new HashMap<>();
            map.put("报事投诉", projectComplaintRecordService.countByOff());
            map.put("报修服务", projectRepairRecordService.countByOff());
            map.put("住户审核", projectHousePersonRelService.countByOff());
            map.put("车辆审核", projectCarPreRegisterService.countByOff());
            map.put("访客审核", projectVisitorHisService.countByOff());
            map.put("警情记录", projectEntranceAlarmEventService.findCount(ProjectContextHolder.getProjectId(),1));
            map.put("周界报警", projectPerimeterAlarmEventService.countByOff());
            map.put("巡更", projectPerimeterAlarmEventService.countByoffxj());
            return R.ok(map);
        }catch (Exception ex){
            ex.printStackTrace();
            return R.failed("数据异常");
        }


    }
}
