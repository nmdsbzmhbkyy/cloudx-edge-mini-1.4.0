package com.aurine.cloudx.open.meta.service;

import com.aurine.cloudx.open.origin.entity.ProjectFaceResources;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-人脸信息管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface MetaFaceInfoService {

    /**
     * 新增人脸信息
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectFaceResources> save(ProjectFaceResources po);

    /**
     * 修改人脸信息
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectFaceResources> update(ProjectFaceResources po);

    /**
     * 删除人脸信息
     *
     * @param id
     * @return
     * @author:
     */
    R<Boolean> delete(String id);

}
