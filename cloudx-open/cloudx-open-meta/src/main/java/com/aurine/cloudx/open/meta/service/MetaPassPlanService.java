package com.aurine.cloudx.open.meta.service;

import com.aurine.cloudx.open.origin.entity.ProjectPassPlan;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-通行方案管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface MetaPassPlanService {

    /**
     * 新增通行方案
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectPassPlan> save(ProjectPassPlan po);

    /**
     * 修改通行方案
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectPassPlan> update(ProjectPassPlan po);

    /**
     * 删除通行方案
     *
     * @param id
     * @return
     * @author:
     */
    R<Boolean> delete(String id);

}
