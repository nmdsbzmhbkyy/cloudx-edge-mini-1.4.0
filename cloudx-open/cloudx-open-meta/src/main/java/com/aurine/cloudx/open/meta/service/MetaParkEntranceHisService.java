package com.aurine.cloudx.open.meta.service;

import com.aurine.cloudx.open.origin.entity.ProjectParkEntranceHis;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-车行记录管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface MetaParkEntranceHisService {

    /**
     * 新增车行记录
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectParkEntranceHis> save(ProjectParkEntranceHis po);

    /**
     * 修改车行记录
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectParkEntranceHis> update(ProjectParkEntranceHis po);

    /**
     * 删除车行记录
     *
     * @param id
     * @return
     * @author:
     */
    R<Boolean> delete(String id);

}
