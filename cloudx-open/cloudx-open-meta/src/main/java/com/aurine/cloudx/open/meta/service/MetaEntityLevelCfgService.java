package com.aurine.cloudx.open.meta.service;

import com.aurine.cloudx.open.origin.entity.ProjectEntityLevelCfg;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-组团配置管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface MetaEntityLevelCfgService {

    /**
     * 新增组团配置
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectEntityLevelCfg> save(ProjectEntityLevelCfg po);

    /**
     * 修改组团配置
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectEntityLevelCfg> update(ProjectEntityLevelCfg po);

    /**
     * 删除组团配置
     *
     * @param id
     * @return
     * @author:
     */
    R<Boolean> delete(String id);

}
