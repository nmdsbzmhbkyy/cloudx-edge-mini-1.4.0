package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.util.IdUtil;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.SysDeviceTypeModel;
import com.aurine.cloudx.estate.service.ProjectZnPasscodeService;
import com.aurine.cloudx.estate.thirdparty.module.device.factory.DeviceFactoryProducer;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.PassWayDeviceService;
import com.aurine.cloudx.estate.vo.zn.SetSupperPasscodeVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ProjectZnPasscodeServiceImpl implements ProjectZnPasscodeService {
    private static List<String> productIds = new ArrayList<String>() {{
        add("TD004M4001");
        add("TD004M2001");
        add("TD004M1001");
    }};
    @Autowired
    private ProjectDeviceInfoServiceImpl deviceInfoService;

    @Override
    public void setSupperPasscode(SetSupperPasscodeVo vo) {
        log.info("【智能多门门禁】批量设置超级密码：{}",vo.getPasscode());
        for (String productId : productIds) {
            PassWayDeviceService passWayDeviceService = DeviceFactoryProducer.getFactoryByProductId(productId).getPassWayDeviceService();
            List<ProjectDeviceInfo> list = deviceInfoService.lambdaQuery().eq(ProjectDeviceInfo::getProductId, productId).list();
            log.info("【智能多门门禁】产品{}->查询到设备：{}",productId,list);
            for (ProjectDeviceInfo deviceInfo : list) {
                passWayDeviceService.setZnSupperPasscode(deviceInfo,vo, IdUtil.randomUUID());
            }
        }
    }
}
