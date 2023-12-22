package com.aurine.cloudx.open.cascade.service;

import com.aurine.cloudx.open.origin.entity.EdgeCascadeProcessMaster;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * 级联入云对接进度
 *
 * @author : linlx
 * @date : 2022 3 24 9:43
 */

public interface CascadeProcessMasterService {

    /**
     * 新增级联入云对接进度
     *
     * @param po
     * @return
     */
    R<EdgeCascadeProcessMaster> save(EdgeCascadeProcessMaster po);

    /**
     * 修改级联入云对接进度
     *
     * @param po
     * @return
     */
    R<EdgeCascadeProcessMaster> update(EdgeCascadeProcessMaster po);
}
