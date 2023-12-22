package com.aurine.cloudx.open.meta.service;

import com.aurine.cloudx.open.origin.entity.ProjectParkingInfo;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-车场信息管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface MetaParkingInfoService {

    /**
     * 新增车场信息
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectParkingInfo> save(ProjectParkingInfo po);

    /**
     * 修改车场信息
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectParkingInfo> update(ProjectParkingInfo po);

    /**
     * 删除车场信息
     *
     * @param id
     * @return
     * @author:
     */
    R<Boolean> delete(String id);

}
