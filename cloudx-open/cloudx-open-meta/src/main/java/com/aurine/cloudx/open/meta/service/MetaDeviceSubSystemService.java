package com.aurine.cloudx.open.meta.service;

import com.aurine.cloudx.open.origin.entity.ProjectDeviceSubsystem;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-设备子系统管理
 *
 * @author : linlx
 * @date : 2022 3 24 9:43
 */

public interface MetaDeviceSubSystemService {

    /**
     * 新增设备子系统
     *
     * @param po
     * @return
     */
    R<ProjectDeviceSubsystem> save(ProjectDeviceSubsystem po);
}
