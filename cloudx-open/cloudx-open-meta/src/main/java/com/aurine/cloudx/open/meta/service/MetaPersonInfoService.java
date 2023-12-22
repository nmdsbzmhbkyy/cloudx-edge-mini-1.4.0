package com.aurine.cloudx.open.meta.service;

import com.aurine.cloudx.open.origin.entity.ProjectPersonInfo;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-人员信息管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface MetaPersonInfoService {

    /**
     * 新增人员信息
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectPersonInfo> save(ProjectPersonInfo po);

    /**
     * 修改人员信息
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectPersonInfo> update(ProjectPersonInfo po);

    /**
     * 删除人员信息
     *
     * @param id
     * @return
     * @author:
     */
    R<Boolean> delete(String id);

}
