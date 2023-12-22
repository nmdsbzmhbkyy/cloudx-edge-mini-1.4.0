package com.aurine.cloudx.open.meta.service;

import com.aurine.cloudx.open.origin.entity.ProjectParkRegion;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-车位区域管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface MetaParkRegionService {

    /**
     * 新增车位区域
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectParkRegion> save(ProjectParkRegion po);

    /**
     * 修改车位区域
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectParkRegion> update(ProjectParkRegion po);

    /**
     * 删除车位区域
     *
     * @param id
     * @return
     * @author:
     */
    R<Boolean> delete(String id);

}
