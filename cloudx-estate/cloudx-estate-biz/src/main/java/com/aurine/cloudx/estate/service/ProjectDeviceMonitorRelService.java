package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectDeviceMonitorRel;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 设备管理-监控设备关联
 *
 * @author 邹宇
 * @date 2021-07-12 13:47:21
 */
public interface ProjectDeviceMonitorRelService extends IService<ProjectDeviceMonitorRel> {

    /**
     * 删除设备和监控的关联信息
     * @param id
     * @return
     */
    boolean removeAll(String id);
}
