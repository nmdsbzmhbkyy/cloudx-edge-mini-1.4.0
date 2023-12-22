package com.aurine.cloudx.open.meta.service;

import com.aurine.cloudx.open.origin.entity.ProjectAlarmHandle;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-报警处理管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface MetaAlarmHandleService {

    /**
     * 新增报警处理
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectAlarmHandle> save(ProjectAlarmHandle po);

    /**
     * 修改报警处理
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectAlarmHandle> update(ProjectAlarmHandle po);

    /**
     * 删除报警处理
     *
     * @param id
     * @return
     * @author:
     */
    R<Boolean> delete(String id);

}
