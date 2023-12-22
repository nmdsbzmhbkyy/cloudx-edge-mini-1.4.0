package com.aurine.cloudx.open.meta.service;

import com.aurine.cloudx.open.origin.entity.ProjectPhysicalPassPolicy;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-物理策略管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface MetaPhysicalPassPolicyService {

    /**
     * 新增物理策略
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectPhysicalPassPolicy> save(ProjectPhysicalPassPolicy po);

    /**
     * 修改物理策略
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectPhysicalPassPolicy> update(ProjectPhysicalPassPolicy po);

    /**
     * 删除物理策略
     *
     * @param id
     * @return
     * @author:
     */
    R<Boolean> delete(String id);

}
