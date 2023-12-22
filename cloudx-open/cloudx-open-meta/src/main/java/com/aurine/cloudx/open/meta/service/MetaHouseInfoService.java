package com.aurine.cloudx.open.meta.service;

import com.aurine.cloudx.open.origin.entity.ProjectHouseInfo;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-房屋信息管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface MetaHouseInfoService {

    /**
     * 新增房屋信息
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectHouseInfo> save(ProjectHouseInfo po);

    /**
     * 修改房屋信息
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectHouseInfo> update(ProjectHouseInfo po);

    /**
     * 删除房屋信息
     *
     * @param id
     * @return
     * @author:
     */
    R<Boolean> delete(String id);

}
