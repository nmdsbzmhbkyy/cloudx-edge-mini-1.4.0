package com.aurine.cloudx.open.meta.service;

import com.aurine.cloudx.open.origin.entity.ProjectRightDevice;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-权限设备关系管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface MetaRightDeviceRelService {

    /**
     * 新增权限设备关系
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectRightDevice> save(ProjectRightDevice po);

    /**
     * 修改权限设备关系
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectRightDevice> update(ProjectRightDevice po);

    /**
     * 删除权限设备关系
     *
     * @param id
     * @return
     * @author:
     */
    R<Boolean> delete(String id);

}
