package com.aurine.cloudx.estate.service.impl;

import com.aurine.cloudx.common.core.constant.DeviceTypeConstants;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.ProjectDeviceMonitorRel;
import com.aurine.cloudx.estate.entity.ProjectPerimeterAlarmArea;
import com.aurine.cloudx.estate.mapper.ProjectDeviceMonitorRelMapper;
import com.aurine.cloudx.estate.service.ProjectDeviceInfoService;
import com.aurine.cloudx.estate.service.ProjectDeviceMonitorRelService;
import com.aurine.cloudx.estate.service.ProjectPerimeterAlarmAreaService;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * 设备管理-监控设备关联
 *
 * @author 邹宇
 * @date 2021-07-12 13:47:21
 */
@Service
public class ProjectDeviceMonitorRelServiceImpl extends ServiceImpl<ProjectDeviceMonitorRelMapper, ProjectDeviceMonitorRel> implements ProjectDeviceMonitorRelService {


    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;


    /**
     * 删除设备和监控的关联信息
     *
     * @param id
     * @return
     */
    @Override
    public boolean removeAll(String id) {
        ProjectDeviceInfo infoServiceOne = projectDeviceInfoService.getOne(Wrappers.lambdaQuery(ProjectDeviceInfo.class).eq(ProjectDeviceInfo::getDeviceId, id));
        //判断设备是否是报警主机，是的是查出下面的防区删除关联信息
        if (infoServiceOne.getDeviceType().equals(DeviceTypeConstants.ALARM_HOST)) {
             return baseMapper.deleteDefenseArea(infoServiceOne.getDeviceId());
        } else {
            List<ProjectDeviceMonitorRel> list = list(Wrappers.lambdaQuery(ProjectDeviceMonitorRel.class).eq(ProjectDeviceMonitorRel::getDeviceId, id));
            if (CollectionUtils.isNotEmpty(list)) {
               return removeByIds(list);
            }
            return true;
        }
    }
}
