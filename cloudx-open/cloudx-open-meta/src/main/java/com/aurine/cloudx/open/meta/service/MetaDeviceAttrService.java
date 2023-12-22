package com.aurine.cloudx.open.meta.service;

import com.aurine.cloudx.open.origin.entity.ProjectDeviceAttr;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-设备拓展属性管理
 *
 * @author : zouyu
 * @date : 2023-05-04 14:00:55
 */

public interface MetaDeviceAttrService {

    /**
     * 新增设备拓展属性
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectDeviceAttr> save(ProjectDeviceAttr po);

    /**
     * 修改设备拓展属性
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectDeviceAttr> update(ProjectDeviceAttr po);

    /**
     * 删除设备拓展属性
     *
     * @param id
     * @return
     * @author:
     */
    R<Boolean> delete(String id);

}
