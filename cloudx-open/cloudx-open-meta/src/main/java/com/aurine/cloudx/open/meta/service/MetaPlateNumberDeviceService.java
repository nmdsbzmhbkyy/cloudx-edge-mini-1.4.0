package com.aurine.cloudx.open.meta.service;

import com.aurine.cloudx.open.origin.entity.ProjectPlateNumberDevice;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-设备车牌号下发情况管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface MetaPlateNumberDeviceService {

    /**
     * 新增设备车牌号下发情况
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectPlateNumberDevice> save(ProjectPlateNumberDevice po);

    /**
     * 修改设备车牌号下发情况
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectPlateNumberDevice> update(ProjectPlateNumberDevice po);

    /**
     * 删除设备车牌号下发情况
     *
     * @param id
     * @return
     * @author:
     */
    R<Boolean> delete(String id);

}
