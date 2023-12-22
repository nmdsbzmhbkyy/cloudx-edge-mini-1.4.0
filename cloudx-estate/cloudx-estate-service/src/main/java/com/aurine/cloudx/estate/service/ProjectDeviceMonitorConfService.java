package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectDeviceMonitorConf;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 设备监控项配置(ProjectDeviceMonitorConf)表服务接口
 *
 * @author 王良俊
 * @since 2020-07-23 18:30:45
 */
public interface ProjectDeviceMonitorConfService extends IService<ProjectDeviceMonitorConf> {

    /**
     * <p>
     *  通过巡检点id获取到期对应的监控项
     * </p>
     *
     * @param
     * @return
     * @throws
    */
    List<ProjectDeviceMonitorConf> listCheckItemListByDeviceId(String deviceId);
}