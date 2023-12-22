package com.aurine.cloudx.open.meta.service;

import com.aurine.cloudx.open.origin.entity.ProjectFrameInfo;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-框架信息管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface MetaFrameInfoService {

    /**
     * 新增框架信息
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectFrameInfo> save(ProjectFrameInfo po);

    /**
     * 修改框架信息
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectFrameInfo> update(ProjectFrameInfo po);

    /**
     * 删除框架信息
     *
     * @param id
     * @return
     * @author:
     */
    R<Boolean> delete(String id);

}
