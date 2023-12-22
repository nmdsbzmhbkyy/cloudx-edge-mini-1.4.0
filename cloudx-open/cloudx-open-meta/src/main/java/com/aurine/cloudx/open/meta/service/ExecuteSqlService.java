package com.aurine.cloudx.open.meta.service;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.origin.entity.ProjectEntranceAlarmEvent;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-报警事件管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface ExecuteSqlService {

    /**
     * 新增报警事件
     *
     * @param model
     * @return
     * @author:
     */
    R<Boolean> save(OpenApiModel model);

    /**
     * 修改报警事件
     *
     * @param model
     * @return
     * @author:
     */
    R<Boolean> update(OpenApiModel model);

    /**
     * 删除报警事件
     *
     * @param model
     * @return
     * @author:
     */
    R<Boolean> delete(OpenApiModel model);

}
