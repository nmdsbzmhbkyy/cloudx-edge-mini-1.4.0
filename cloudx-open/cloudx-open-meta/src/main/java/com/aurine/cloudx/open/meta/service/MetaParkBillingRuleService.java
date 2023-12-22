package com.aurine.cloudx.open.meta.service;

import com.aurine.cloudx.open.origin.entity.ProjectParkBillingRule;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-车场计费规则管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface MetaParkBillingRuleService {

    /**
     * 新增车场计费规则
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectParkBillingRule> save(ProjectParkBillingRule po);

    /**
     * 修改车场计费规则
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectParkBillingRule> update(ProjectParkBillingRule po);

    /**
     * 删除车场计费规则
     *
     * @param id
     * @return
     * @author:
     */
    R<Boolean> delete(String id);

}
