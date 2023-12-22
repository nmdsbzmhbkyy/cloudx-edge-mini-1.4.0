package com.aurine.cloudx.open.meta.service;

import com.aurine.cloudx.open.origin.entity.ProjectVisitor;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-访客信息管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface MetaVisitorInfoService {

    /**
     * 新增访客信息
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectVisitor> save(ProjectVisitor po);

    /**
     * 修改访客信息
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectVisitor> update(ProjectVisitor po);

    /**
     * 删除访客信息
     *
     * @param id
     * @return
     * @author:
     */
    R<Boolean> delete(String id);

}
