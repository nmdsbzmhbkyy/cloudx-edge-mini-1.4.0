package com.aurine.cloudx.open.meta.service;

import com.aurine.cloudx.open.origin.entity.ProjectParkingPlace;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-车位管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface MetaParkingPlaceService {

    /**
     * 新增车位
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectParkingPlace> save(ProjectParkingPlace po);

    /**
     * 修改车位
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectParkingPlace> update(ProjectParkingPlace po);

    /**
     * 删除车位
     *
     * @param id
     * @return
     * @author:
     */
    R<Boolean> delete(String id);

}
