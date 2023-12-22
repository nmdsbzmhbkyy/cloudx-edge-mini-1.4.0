package com.aurine.cloudx.open.meta.service;

import com.aurine.cloudx.open.origin.entity.ProjectLogicPassPolicy;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-逻辑策略管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface MetaLogicPassPolicyService {

    /**
     * 新增逻辑策略
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectLogicPassPolicy> save(ProjectLogicPassPolicy po);

    /**
     * 修改逻辑策略
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectLogicPassPolicy> update(ProjectLogicPassPolicy po);

    /**
     * 删除逻辑策略
     *
     * @param id
     * @return
     * @author:
     */
    R<Boolean> delete(String id);

}
