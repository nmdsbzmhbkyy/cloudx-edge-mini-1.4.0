package com.aurine.cloudx.open.meta.service;

import com.aurine.cloudx.open.origin.entity.ProjectVisitorHis;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-来访记录管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface MetaVisitorHistService {

    /**
     * 新增来访记录
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectVisitorHis> save(ProjectVisitorHis po);

    /**
     * 修改来访记录
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectVisitorHis> update(ProjectVisitorHis po);

    /**
     * 删除来访记录
     *
     * @param id
     * @return
     * @author:
     */
    R<Boolean> delete(String id);

}
