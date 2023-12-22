package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.exception.handle.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.aurine.cloudx.estate.constant.enums.device.DeviceRegAbnormalEnum;
import com.aurine.cloudx.estate.constant.enums.device.DeviceRegParamEnum;
import com.aurine.cloudx.estate.entity.ProjectDeviceAbnormal;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.service.ProjectDeviceInfoService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.exception.entity.DeviceAbnormalHandleInfo;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.exception.handle.base.AbstractExceptionHandler;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * <p>用于判断MAC地址是否异常（重复）</p>
 *
 * @author : 王良俊
 * @date : 2021-09-26 09:07:04
 */
@Component
@Slf4j
public class MacExceptionHandler extends AbstractExceptionHandler {

    @Resource
    ProjectDeviceInfoService projectDeviceInfoServiceImpl;

    @Override
    public void handle(DeviceAbnormalHandleInfo info, ProjectDeviceAbnormal abnormal) {

        String mac = (String) info.getParam(DeviceRegParamEnum.MAC);
        abnormal.setMac(mac);
        if (StrUtil.isNotEmpty(mac)) {
            int count = projectDeviceInfoServiceImpl.count(new LambdaQueryWrapper<ProjectDeviceInfo>()
                    .eq(ProjectDeviceInfo::getMac, mac)
                    .ne(StrUtil.isNotEmpty(info.getSn()), ProjectDeviceInfo::getSn, info.getSn())
                    .ne(StrUtil.isNotEmpty(info.getDeviceId()), ProjectDeviceInfo::getDeviceId, info.getDeviceId())
                    .ne(StrUtil.isNotEmpty(info.getThirdpartyCode()), ProjectDeviceInfo::getThirdpartyCode, info.getThirdpartyCode())
            );
            adjustAbnormal(abnormal, DeviceRegAbnormalEnum.AURINE_MAC_REPEAT, count > 0);
            log.info("结束MAC异常判断：{}", JSON.toJSONString(abnormal));
        }
    }

    @Override
    public DeviceRegParamEnum getTargetParam() {
        return DeviceRegParamEnum.MAC;
    }

}
