package com.aurine.cloudx.open.meta.service;

import com.aurine.cloudx.open.origin.entity.ProjectDeviceRegion;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-项目区域管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface MetaProjectRegionService {

    /**
     * 新增
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectDeviceRegion> save(ProjectDeviceRegion po);

    /**
     * 修改
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectDeviceRegion> update(ProjectDeviceRegion po);

    /**
     * 删除
     *
     * @param id
     * @return
     * @author:
     */
    R<Boolean> delete(String id);

}
