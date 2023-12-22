//package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.task;
//
//import cn.hutool.core.collection.CollUtil;
//import com.aurine.cloudx.estate.constant.DeviceConstant;
//import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
//import com.aurine.cloudx.estate.service.ProjectDeviceInfoService;
//import com.aurine.cloudx.estate.util.RedisUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author:zy
// * @data:2022-12-19 12:15:41
// */
//@Component
//@Slf4j
//@EnableScheduling
//public class DeviceSubscribeTask {
//
//    @Resource
//    private ProjectDeviceInfoService projectDeviceInfoService;
//
//    @Scheduled(fixedDelay = 3000)
//    private void scheduled(){
//        List<Object> list = RedisUtil.lGet(DeviceConstant.DEVICE_UPDATE, 0, -1);
//
//        List<ProjectDeviceInfo> deviceList = new ArrayList<>();
//        if (CollUtil.isNotEmpty(list)) {
//            for (Object object : list) {
//                deviceList.add((ProjectDeviceInfo) object);
//            }
//            projectDeviceInfoService.updateBatchById(deviceList);
//            for (Object o : list) {
//                RedisUtil.leftPopKey(DeviceConstant.DEVICE_UPDATE);
//            }
//            log.info("[设备更新] 批量更新设备状态:{}",list.size());
//        }
//    }
//}
