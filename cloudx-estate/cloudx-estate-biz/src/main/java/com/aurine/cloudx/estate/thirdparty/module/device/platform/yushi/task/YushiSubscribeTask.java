package com.aurine.cloudx.estate.thirdparty.module.device.platform.yushi.task;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.constant.DeviceTypeConstants;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.estate.constant.enums.DeviceStatusEnum;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.yushi.entity.request.YushiConnectDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.yushi.remote.factory.YushiRemoteDeviceServiceFactory;
import com.aurine.cloudx.estate.util.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@EnableScheduling
@Configuration
@ConditionalOnProperty(name = "yushi.subscribe.enable", havingValue = "true", matchIfMissing = true)
public class YushiSubscribeTask {

    /**
     * @Scheduled(fixedRate = 6000) ：上一次开始执行时间点之后6秒再执行
     * @Scheduled(fixedDelay = 6000) ：上一次执行完毕时间点之后6秒再执行
     * @Scheduled(initialDelay=1000, fixedRate=6000) ：第一次延迟1秒后执行，之后按fixedRate的规则每6秒执行一次
     * @Scheduled(cron=""):详见cron表达式http://www.pppet.net/
     */

    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;
    @Resource
    private SysThirdPartyInterfaceConfigService sysThirdPartyInterfaceConfigService;
    @Resource
    private SysDeviceProductMapService sysDeviceProductMapService;
    @Resource
    private ProjectDeviceAttrService projectDeviceAttrService;
    @Resource
    private ProjectDeviceAttrConfService projectDeviceAttrConfService;

    //    @Scheduled(cron = "*/5 * * * * ?")
    @Scheduled(cron = "0 */5 * * * ?")
    private void scheduled() {

        SysThirdPartyInterfaceConfig thirdPartyInterfaceConfig = sysThirdPartyInterfaceConfigService.getOne(new LambdaQueryWrapper<SysThirdPartyInterfaceConfig>()
                .eq(SysThirdPartyInterfaceConfig::getName, PlatformEnum.YUSHI.value).eq(SysThirdPartyInterfaceConfig::getType, PlatformEnum.YUSHI.type));
        if (thirdPartyInterfaceConfig == null) return;
        log.info("执行宇视定时刷新订阅");

        List<SysDeviceProductMap> productMapList = sysDeviceProductMapService.list(new LambdaQueryWrapper<SysDeviceProductMap>()
                .eq(SysDeviceProductMap::getThirdPartyNo, thirdPartyInterfaceConfig.getThirdPartyNo()));
        if (CollectionUtil.isEmpty(productMapList)) return;
        List<String> productIdList = productMapList.stream().map(SysDeviceProductMap::getProductId).collect(Collectors.toList());

        List<ProjectDeviceInfo> deviceInfoList = projectDeviceInfoService.list(new LambdaQueryWrapper<ProjectDeviceInfo>().in(ProjectDeviceInfo::getProductId, productIdList));

        deviceInfoList.forEach(device -> {
            YushiConnectDTO connectDTO = YushiConnectDTO.builder().ip(device.getIpv4()).port(device.getPort())
                    .userName(device.getCompanyAccount()).password(device.getCompanyPasswd()).build();

            Boolean flag = false;
            Integer subscribeId = (Integer) RedisUtil.get("yushi:subscribe:" + device.getDeviceId());
            if (subscribeId == null) {
                flag = true;
                log.info("开始宇视设备订阅事件，deviceId:{}, IP:{}", device.getDeviceId(), device.getIpv4());
            } else {
                try {
                    JSONObject data = YushiRemoteDeviceServiceFactory.getInstance(VersionEnum.V1).eventSubscribeRefresh(connectDTO, subscribeId);
                    RedisUtil.set("yushi:subscribe:" + device.getDeviceId(), subscribeId, 3600);
                    device.setStatus(DeviceStatusEnum.ONLINE.code);
                } catch (Exception e) {
                    flag = true;
                    log.info("宇视设备订阅刷新失败，deviceId:{}, IP:{} 尝试重新订阅", device.getDeviceId(), device.getIpv4());
                }
            }

            //第一次订阅或者订阅失败后重新订阅
            if (flag) {
                refreshDevice(device, connectDTO);
            }
            projectDeviceInfoService.updateById(device);
        });
    }

    private void refreshDevice(ProjectDeviceInfo device, YushiConnectDTO connectDTO) {
        try {

            if (StringUtil.isEmpty(device.getThirdpartyCode())) {
                // 获取设备信息
                JSONObject data = YushiRemoteDeviceServiceFactory.getInstance(VersionEnum.V1).systemDeviceInfo(connectDTO);
                device.setThirdpartyCode(data.getString("SerialNumber"));
            }

            // 订阅设备事件
            JSONObject data1 = YushiRemoteDeviceServiceFactory.getInstance(VersionEnum.V1).eventSubscribe(connectDTO);
            Integer subscribeId = data1.getInteger("ID");
            RedisUtil.set("yushi:subscribe:" + device.getDeviceId(), subscribeId, 3600);

            // 获取视频流rtsp
            ProjectDeviceAttr videoUrlAttr = projectDeviceAttrService.getOne(new LambdaQueryWrapper<ProjectDeviceAttr>()
                    .eq(ProjectDeviceAttr::getDeviceId, device.getDeviceId()).eq(ProjectDeviceAttr::getAttrCode, "videoUrl"));
            if (videoUrlAttr == null) {
                JSONObject data2 = YushiRemoteDeviceServiceFactory.getInstance(VersionEnum.V1).videoLiveStream(connectDTO);
                ProjectDeviceAttrConf attrConf = projectDeviceAttrConfService.getOne(new LambdaQueryWrapper<ProjectDeviceAttrConf>()
                        .eq(ProjectDeviceAttrConf::getAttrCode, "videoUrl").eq(ProjectDeviceAttrConf::getDeviceTypeId, DeviceTypeConstants.MONITOR_DEVICE));
                ProjectDeviceAttr attr = new ProjectDeviceAttr();
                attr.setAttrId(attrConf.getAttrId());
                attr.setDeviceId(device.getDeviceId());
                attr.setAttrCode("videoUrl");
                attr.setDeviceType(DeviceTypeConstants.MONITOR_DEVICE);
                attr.setProjectId(device.getProjectId());
                TenantContextHolder.setTenantId(1);
                attr.setAttrValue(data2.getString("URL"));
                projectDeviceAttrService.save(attr);
            }

            device.setStatus(DeviceStatusEnum.ONLINE.code);
        } catch (Exception e) {
            log.info("宇视设备订阅刷新失败，deviceId:{}, IP:{}", device.getDeviceId(), device.getIpv4());
            e.printStackTrace();
            device.setStatus(DeviceStatusEnum.OFFLINE.code);
        }
    }

}