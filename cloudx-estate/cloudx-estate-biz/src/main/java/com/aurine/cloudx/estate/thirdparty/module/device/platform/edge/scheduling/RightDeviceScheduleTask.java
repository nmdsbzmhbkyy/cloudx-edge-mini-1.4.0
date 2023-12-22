package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.scheduling;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.PassRightCertDownloadStatusEnum;
import com.aurine.cloudx.estate.entity.ProjectRightDevice;
import com.aurine.cloudx.estate.entity.ProjectRightDeviceCache;
import com.aurine.cloudx.estate.service.ProjectCardHisService;
import com.aurine.cloudx.estate.service.ProjectDeviceInfoProxyService;
import com.aurine.cloudx.estate.service.ProjectRightDeviceCacheService;
import com.aurine.cloudx.estate.service.ProjectRightDeviceService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant.AurineEdgeServiceEnum;
import com.aurine.cloudx.estate.util.RedisUtil;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoProxyVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author:zy
 * @data:2022/12/16 3:50 下午
 */
@Component
@Slf4j
@EnableScheduling
public class RightDeviceScheduleTask {

    @Resource
    private ProjectRightDeviceCacheService projectRightDeviceCacheService;

    @Resource
    private ProjectRightDeviceService projectRightDeviceService;
    @Resource
    private ProjectCardHisService projectCardHisService;
    @Resource
    private ProjectDeviceInfoProxyService projectDeviceInfoProxyService;


    /**
     * 处理凭证任务
     */
//    @Scheduled(fixedDelay = 1000 * 30)
//    private void task() {
//        try {
//            List<ProjectRightDeviceCache> rightDeviceCacheList = projectRightDeviceCacheService.getRightDeviceCache();
//            if (CollUtil.isNotEmpty(rightDeviceCacheList)) {
//                log.info("[凭证任务] rightDeviceCacheList长度:{}", rightDeviceCacheList.size());
//                rightDeviceCacheList.forEach(e -> {
//                    if (e.getType().equals(AurineEdgeServiceEnum.CERT_CARD.cloudCode)) {
//                        //更新凭证状态
//                        projectRightDeviceService.update(Wrappers.lambdaUpdate(ProjectRightDevice.class)
//                                .set(ProjectRightDevice::getDlStatus, e.getState())
//                                .eq(ProjectRightDevice::getDeviceId, e.getDeviceId())
//                                .eq(ProjectRightDevice::getCertMediaInfo, e.getPassNo()));
//                    } else if (e.getType().equals(AurineEdgeServiceEnum.CERT_FACE.cloudCode)) {
//                        //更新凭证状态
//                        projectRightDeviceService.update(Wrappers.lambdaUpdate(ProjectRightDevice.class)
//                                .set(ProjectRightDevice::getDlStatus, e.getState())
//                                .eq(ProjectRightDevice::getDeviceId, e.getDeviceId())
//                                .eq(ProjectRightDevice::getCertMediaCode, e.getPassNo()));
//                    }
//                });
//
//                List<String> uuidList = rightDeviceCacheList.stream().map(ProjectRightDeviceCache::getUuid).collect(Collectors.toList());
//                //处理成功删除缓存数据
//                projectRightDeviceCacheService.removeByIds(uuidList);
//            }
//        } catch (Exception e) {
//        }
//    }

    /**
     * 处理卡操作记录
     */
    @Scheduled(fixedDelay = 1000 * 60 * 5)
    private void handleCardHis() {
        try {
            projectCardHisService.handleCardHis();
        } catch (Exception e) {
        }
    }



    /**
     * 通行凭证下发定时任务  0 0 1,3 * * ?  每天凌晨1点和3点执行
     * */
    @Scheduled(cron = "0 0 1,3 * * ?")
    public void passRightDownloadHandler() {
        try {
            LocalDateTime limitTime = LocalDateTime.now().minusMinutes(10);
            List<ProjectRightDevice> list = projectRightDeviceService.list(Wrappers.lambdaQuery(ProjectRightDevice.class)
                    .in(ProjectRightDevice::getDlStatus, PassRightCertDownloadStatusEnum.DOWNLOADING.code, PassRightCertDownloadStatusEnum.FAIL.code)
                    .le(ProjectRightDevice::getUpdateTime, limitTime));
            if (list!=null && list.size()>0) {
                log.info("查询到{}条凭证需要重新下发", list.size());
                int count = 0;
                Map<String, List<ProjectRightDevice>> map = list.stream().collect(Collectors.groupingBy(ProjectRightDevice::getDeviceId));
                for (List<ProjectRightDevice> certs : map.values()) {
                    try {
                        ProjectDeviceInfoProxyVo deviceInfo = projectDeviceInfoProxyService.getVoById(certs.get(0).getDeviceId());
                        ProjectContextHolder.setProjectId(deviceInfo.getProjectId());
                        TenantContextHolder.setTenantId(1);
                        projectRightDeviceService.addCerts(certs, true, false, false);
                        count = count + certs.size();
                    }
                    catch (Exception e) {
                        log.error("执行下发凭证异常，设备ID：" + certs.get(0).getDeviceId(),  e);
                    }
                }
            }
        } catch (Exception e) {
        }
    }


    /**
     * 删除卡片定时任务
     * */
    @Scheduled(cron = "0 */1 * * * ?")
    public void delCard() {
        try {
            List<Object> delCardList = RedisUtil.lGet("DelCardList", 0, -1);
            if(CollUtil.isNotEmpty(delCardList)){
                log.info("删除卡片定时任务执行 数据:{}", JSONObject.toJSONString(delCardList));
                List<ProjectRightDevice> rightDeviceList = projectRightDeviceService.list(new QueryWrapper<ProjectRightDevice>().lambda()
                        .in(ProjectRightDevice::getCertMediaId, delCardList));
                projectRightDeviceService.delCerts(rightDeviceList);
                RedisUtil.del("DelCardList");
            }
        } catch (Exception e) {
        }
    }
}
