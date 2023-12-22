package com.aurine.cloudx.open.meta.service;

import com.aurine.cloudx.open.origin.entity.ProjectPersonDevice;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-人员设备权限关系管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface MetaPersonDeviceRelService {

    /**
     * 新增人员设备权限关系
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectPersonDevice> save(ProjectPersonDevice po);

    /**
     * 修改人员设备权限关系
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectPersonDevice> update(ProjectPersonDevice po);

    /**
     * 删除人员设备权限关系
     *
     * @param id
     * @return
     * @author:
     */
    R<Boolean> delete(String id);

}
