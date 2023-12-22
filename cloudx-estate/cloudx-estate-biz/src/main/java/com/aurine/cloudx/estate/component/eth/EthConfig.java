package com.aurine.cloudx.estate.component.eth;

import cn.hutool.core.bean.BeanUtil;
import com.aurine.cloudx.estate.config.ServerInfoConfig;
import com.aurine.cloudx.estate.service.ProjectDeviceInfoService;
import com.aurine.cloudx.estate.vo.DeviceNetworkInfoVo;
import com.aurine.tools.eth.EthService;
import com.aurine.tools.eth.EthServiceImpl;
import com.aurine.tools.eth.protocol.Data0x22ResponseDeviceGetNetConfig;
import com.aurine.tools.eth.protocol.pojo.NetParams;
import com.aurine.tools.eth.protocol.pojo.NoInfo;
import com.aurine.tools.eth.protocol.pojo.NoRules;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @Auther: hjj
 * @Date: 2022/6/11 15:47
 * @Description:
 */
@Configuration
public class EthConfig {
    @Resource
    ProjectDeviceInfoService projectDeviceInfoService;
    @Resource
    ServerInfoConfig serverInfoConfig;
    @Bean
    public EthService initEth(){
        System.out.println("=========initEth=========");
        EthService service = new EthServiceImpl();
        // 通讯库对mac格式要求： 十六进制小写，冒号隔开
        service.update0x21Listener((deviceMac) -> {
            // 广播
            System.out.println("设备: " + deviceMac + " 广播查找上位机");
        });

        service.update0x22Listener((deviceMac) -> {
            // 请求配网
            System.out.println("设备: " + deviceMac + " 配网请求");

            DeviceNetworkInfoVo netWorkInfo = projectDeviceInfoService.getDeviceNetWorkInfo(deviceMac);
            if(netWorkInfo == null) {
                return null;
            }
            Data0x22ResponseDeviceGetNetConfig curConf = new Data0x22ResponseDeviceGetNetConfig();
            curConf.setNetParams(BeanUtil.copyProperties(netWorkInfo.getNetParams(),NetParams.class));
            curConf.setNoInfo(BeanUtil.copyProperties(netWorkInfo.getNoInfo(), NoInfo.class));
            curConf.setNoRules(BeanUtil.copyProperties(netWorkInfo.getNoRules(), NoRules.class));
            curConf.setLanguage(2052);
            curConf.setSectionDesc(netWorkInfo.getSectionDesc());
            return curConf;
        });

        service.update0x23Listener((deviceMac, result)->{
            // 更新数据
            System.out.println("设备: " + deviceMac + " 更新配置结果");
            projectDeviceInfoService.updateConfigured(deviceMac);
        });
        return service;
    };
}
