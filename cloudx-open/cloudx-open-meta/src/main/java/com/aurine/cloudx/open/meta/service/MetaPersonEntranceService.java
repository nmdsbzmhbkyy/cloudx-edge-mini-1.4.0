package com.aurine.cloudx.open.meta.service;

import com.aurine.cloudx.open.origin.entity.ProjectEntranceEvent;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-人行事件管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface MetaPersonEntranceService {

    /**
     * 新增人行事件
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectEntranceEvent> save(ProjectEntranceEvent po);

    /**
     * 修改人行事件
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectEntranceEvent> update(ProjectEntranceEvent po);

    /**
     * 删除人行事件
     *
     * @param id
     * @return
     * @author:
     */
    R<Boolean> delete(String id);

}
