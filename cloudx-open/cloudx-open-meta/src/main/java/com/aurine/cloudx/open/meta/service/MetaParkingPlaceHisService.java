package com.aurine.cloudx.open.meta.service;

import com.aurine.cloudx.open.origin.entity.ProjectParkingPlaceHis;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-车位变动记录管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface MetaParkingPlaceHisService {

    /**
     * 新增车位变动记录
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectParkingPlaceHis> save(ProjectParkingPlaceHis po);

    /**
     * 修改车位变动记录
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectParkingPlaceHis> update(ProjectParkingPlaceHis po);

    /**
     * 删除车位变动记录
     *
     * @param id
     * @return
     * @author:
     */
    R<Boolean> delete(String id);

}
