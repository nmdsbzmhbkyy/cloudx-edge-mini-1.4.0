package com.aurine.cloudx.open.meta.service;

import com.aurine.cloudx.open.origin.entity.ProjectDeviceRel;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-设备关系管理
 *
 * @author : Qiu
 * @date : 2022 06 13 16:45
 */

public interface MetaDeviceRelService {

    /**
     * 新增设备关系
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectDeviceRel> save(ProjectDeviceRel po);

    /**
     * 修改设备关系
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectDeviceRel> update(ProjectDeviceRel po);

    /**
     * 删除设备关系
     *
     * @param id
     * @return
     * @author:
     */
    R<Boolean> delete(String id);

}
