package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.exception.handle.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.device.DeviceRegAbnormalEnum;
import com.aurine.cloudx.estate.constant.enums.device.DeviceRegParamEnum;
import com.aurine.cloudx.estate.entity.ProjectDeviceAbnormal;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.ProjectEntityLevelCfg;
import com.aurine.cloudx.estate.service.ProjectDeviceAbnormalService;
import com.aurine.cloudx.estate.service.ProjectDeviceInfoService;
import com.aurine.cloudx.estate.service.ProjectEntityLevelCfgService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.exception.entity.DeviceAbnormalHandleInfo;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.exception.handle.base.AbstractExceptionHandler;
import com.aurine.cloudx.estate.util.delay.TaskUtil;
import com.aurine.cloudx.estate.vo.ProjectDeviceNoRule;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * <p>用于判断设备编号是否异常（重复）</p>
 *
 * @author : 王良俊
 * @date : 2021-09-26 09:07:04
 */
@Slf4j
@Component
public class DeviceNoExceptionHandler extends AbstractExceptionHandler {

    @Resource
    ProjectDeviceInfoService projectDeviceInfoServiceImpl;

    @Resource
    ProjectEntityLevelCfgService projectEntityLevelCfgService;

    @Resource
    ProjectDeviceAbnormalService projectDeviceAbnormalService;

    @Override
    public void handle(DeviceAbnormalHandleInfo info, ProjectDeviceAbnormal abnormal) {

        String deviceNo = (String) info.getParam(DeviceRegParamEnum.DEVICE_NO);
        // 设备编号系统和中台都可能判断重复如果中台已经告诉设备编号重复了，这里就不在系统中判断
        if (StrUtil.isNotEmpty(info.getFailCode()) && DeviceRegAbnormalEnum.DEVICE_NO_REPEAT.thirdCode.equals(info.getFailCode())) {
            return;
        }
        abnormal.setDeviceCode(deviceNo);
        if (StrUtil.isNotEmpty(deviceNo)) {
            ProjectDeviceNoRule projectSubSection = projectEntityLevelCfgService.getProjectSubSection(ProjectContextHolder.getProjectId());
            String subSection = projectSubSection.getSubSection();
            log.info("设备编号异常判断，获取到的项目编号规则：{}", subSection);
            char[] chars = subSection.toCharArray();
            int length = 1;
            for (char numChar : chars) {
                length += (int) numChar - 48;
            }
            if (length != deviceNo.length()) {
                adjustAbnormal(abnormal, DeviceRegAbnormalEnum.NORMAL, true);
                log.info("设备编号异常判断，检测到设备编号长度和项目框架号不一致加入异常：{}", subSection);
            }
            int count = projectDeviceInfoServiceImpl.count(new LambdaQueryWrapper<ProjectDeviceInfo>()
                    .eq(ProjectDeviceInfo::getDeviceCode, deviceNo)
                    .ne(StrUtil.isNotEmpty(info.getSn()), ProjectDeviceInfo::getSn, info.getSn())
                    .ne(StrUtil.isNotEmpty(info.getDeviceId()), ProjectDeviceInfo::getDeviceId, info.getDeviceId())
                    .ne(StrUtil.isNotEmpty(info.getThirdpartyCode()), ProjectDeviceInfo::getThirdpartyCode, info.getThirdpartyCode())
            );
            adjustAbnormal(abnormal, DeviceRegAbnormalEnum.DEVICE_NO_REPEAT, count > 0);
            log.info("结束设备编号异常判断：{}", JSON.toJSONString(abnormal));
        }
    }

    @Override
    public DeviceRegParamEnum getTargetParam() {
        return DeviceRegParamEnum.DEVICE_NO;
    }

}
