package com.aurine.cloudx.open.meta.service;

import com.aurine.cloudx.open.origin.entity.ProjectLabelConfig;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-人员标签管理
 *
 * @author : linlx
 * @date : 2022 3 24 9:43
 */

public interface MetaLabelConfigService {

    /**
     * 新增人员标签
     *
     * @param po
     * @return
     */
    R<ProjectLabelConfig> save(ProjectLabelConfig po);

    /**
     * 修改人员标签
     *
     * @param po
     * @return
     */
    R<ProjectLabelConfig> update(ProjectLabelConfig po);

    /**
     * 删除人员标签
     *
     * @param id
     * @return
     */
    R<Boolean> delete(String id);
}
