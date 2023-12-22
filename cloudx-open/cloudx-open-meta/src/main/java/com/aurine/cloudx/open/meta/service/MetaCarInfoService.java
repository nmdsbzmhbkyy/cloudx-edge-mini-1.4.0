package com.aurine.cloudx.open.meta.service;

import com.aurine.cloudx.open.origin.entity.ProjectCarInfo;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-车辆信息管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface MetaCarInfoService {

    /**
     * 新增车辆信息
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectCarInfo> save(ProjectCarInfo po);

    /**
     * 修改车辆信息
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectCarInfo> update(ProjectCarInfo po);

    /**
     * 删除车辆信息
     *
     * @param id
     * @return
     * @author:
     */
    R<Boolean> delete(String id);

}
