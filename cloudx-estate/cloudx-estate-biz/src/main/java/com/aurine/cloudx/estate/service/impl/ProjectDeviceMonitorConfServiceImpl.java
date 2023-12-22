package com.aurine.cloudx.estate.service.impl;

import com.aurine.cloudx.estate.entity.ProjectDeviceMonitorConf;
import com.aurine.cloudx.estate.mapper.ProjectDeviceMonitorConfMapper;
import com.aurine.cloudx.estate.service.ProjectDeviceMonitorConfService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 设备监控项配置(ProjectDeviceMonitorConf)表服务实现类
 *
 * @author 王良俊
 * @since 2020-07-23 18:30:45
 */
@Service
public class ProjectDeviceMonitorConfServiceImpl extends ServiceImpl<ProjectDeviceMonitorConfMapper, ProjectDeviceMonitorConf> implements ProjectDeviceMonitorConfService {

    @Autowired
    ProjectDeviceMonitorConfMapper projectDeviceMonitorConfMapper;

    @Override
    public List<ProjectDeviceMonitorConf> listCheckItemListByDeviceId(String deviceId) {
        return projectDeviceMonitorConfMapper.listMoitorByDeviceId(deviceId);
    }
}