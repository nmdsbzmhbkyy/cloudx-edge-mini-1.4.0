package com.aurine.cloudx.open.meta.service;

import com.aurine.cloudx.open.origin.entity.ProjectPersonPlanRel;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-人员通行方案关系管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface MetaPersonPlanRelService {

    /**
     * 新增人员通行方案关系
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectPersonPlanRel> save(ProjectPersonPlanRel po);

    /**
     * 修改人员通行方案关系
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectPersonPlanRel> update(ProjectPersonPlanRel po);

    /**
     * 删除人员通行方案关系
     *
     * @param id
     * @return
     * @author:
     */
    R<Boolean> delete(String id);

}
