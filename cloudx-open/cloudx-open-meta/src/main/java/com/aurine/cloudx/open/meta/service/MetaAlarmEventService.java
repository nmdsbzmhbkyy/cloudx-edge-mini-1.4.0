package com.aurine.cloudx.open.meta.service;

import com.aurine.cloudx.open.origin.entity.ProjectEntranceAlarmEvent;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-报警事件管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface MetaAlarmEventService {

    /**
     * 新增报警事件
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectEntranceAlarmEvent> save(ProjectEntranceAlarmEvent po);

    /**
     * 修改报警事件
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectEntranceAlarmEvent> update(ProjectEntranceAlarmEvent po);

    /**
     * 删除报警事件
     *
     * @param id
     * @return
     * @author:
     */
    R<Boolean> delete(String id);

}
