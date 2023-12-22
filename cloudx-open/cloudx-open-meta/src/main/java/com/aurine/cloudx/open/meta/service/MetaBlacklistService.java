package com.aurine.cloudx.open.meta.service;

import com.aurine.cloudx.open.origin.entity.ProjectBlacklist;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-黑名单管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface MetaBlacklistService {

    /**
     * 新增黑名单
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectBlacklist> save(ProjectBlacklist po);

    /**
     * 修改黑名单
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectBlacklist> update(ProjectBlacklist po);

    /**
     * 删除黑名单
     *
     * @param id
     * @return
     * @author:
     */
    R<Boolean> delete(String id);

}
