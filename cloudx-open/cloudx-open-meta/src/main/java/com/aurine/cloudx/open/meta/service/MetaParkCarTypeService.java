package com.aurine.cloudx.open.meta.service;

import com.aurine.cloudx.open.origin.entity.ProjectParkCarType;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-车辆类型管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface MetaParkCarTypeService {

    /**
     * 新增车辆类型
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectParkCarType> save(ProjectParkCarType po);

    /**
     * 修改车辆类型
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectParkCarType> update(ProjectParkCarType po);

    /**
     * 删除车辆类型
     *
     * @param id
     * @return
     * @author:
     */
    R<Boolean> delete(String id);

}
