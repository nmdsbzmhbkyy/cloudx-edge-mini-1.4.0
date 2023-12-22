package com.aurine.cloudx.open.meta.service;

import com.aurine.cloudx.open.origin.entity.ProjectBuildingInfo;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-楼栋信息管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface MetaBuildingInfoService {

    /**
     * 新增楼栋信息
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectBuildingInfo> save(ProjectBuildingInfo po);

    /**
     * 修改楼栋信息
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectBuildingInfo> update(ProjectBuildingInfo po);

    /**
     * 删除楼栋信息
     *
     * @param id
     * @return
     * @author:
     */
    R<Boolean> delete(String id);

}
