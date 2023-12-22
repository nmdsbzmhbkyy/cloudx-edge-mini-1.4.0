package com.aurine.cloudx.open.meta.service;

import com.aurine.cloudx.open.origin.entity.ProjectPassPlanPolicyRel;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-通行方案策略关系管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface MetaPassPlanPolicyRelService {

    /**
     * 新增通行方案策略关系
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectPassPlanPolicyRel> save(ProjectPassPlanPolicyRel po);

    /**
     * 修改通行方案策略关系
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectPassPlanPolicyRel> update(ProjectPassPlanPolicyRel po);

    /**
     * 删除通行方案策略关系
     *
     * @param id
     * @return
     * @author:
     */
    R<Boolean> delete(String id);

}
