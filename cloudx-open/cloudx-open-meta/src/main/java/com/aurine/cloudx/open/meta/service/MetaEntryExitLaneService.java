package com.aurine.cloudx.open.meta.service;

import com.aurine.cloudx.open.origin.entity.ProjectEntryExitLane;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-出入口车道管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface MetaEntryExitLaneService {

    /**
     * 新增出入口车道
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectEntryExitLane> save(ProjectEntryExitLane po);

    /**
     * 修改出入口车道
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectEntryExitLane> update(ProjectEntryExitLane po);

    /**
     * 删除出入口车道
     *
     * @param id
     * @return
     * @author:
     */
    R<Boolean> delete(String id);

}
