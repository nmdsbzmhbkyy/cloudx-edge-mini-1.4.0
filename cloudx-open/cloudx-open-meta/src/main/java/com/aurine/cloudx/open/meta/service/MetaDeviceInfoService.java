package com.aurine.cloudx.open.meta.service;

import com.aurine.cloudx.open.origin.entity.ProjectDeviceInfo;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-设备信息管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface MetaDeviceInfoService {

    /**
     * 新增设备信息
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectDeviceInfo> save(ProjectDeviceInfo po);

    /**
     * 修改设备信息
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectDeviceInfo> update(ProjectDeviceInfo po);

    /**
     * 删除设备信息
     *
     * @param id
     * @return
     * @author:
     */
    R<Boolean> delete(String id);

}
