package com.aurine.cloudx.open.meta.service;

import com.aurine.cloudx.open.origin.entity.ProjectHousePersonRel;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-住户信息管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface MetaHousePersonInfoService {

    /**
     * 新增住户信息
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectHousePersonRel> save(ProjectHousePersonRel po);

    /**
     * 修改住户信息
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectHousePersonRel> update(ProjectHousePersonRel po);

    /**
     * 删除住户信息
     *
     * @param id
     * @return
     * @author:
     */
    R<Boolean> delete(String id);

}
