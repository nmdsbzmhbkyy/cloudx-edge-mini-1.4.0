package com.aurine.cloudx.open.meta.service;

import com.aurine.cloudx.open.origin.entity.ProjectHouseDesign;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-户型管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface MetaHouseDesignService {

    /**
     * 新增户型
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectHouseDesign> save(ProjectHouseDesign po);

    /**
     * 修改户型
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectHouseDesign> update(ProjectHouseDesign po);

    /**
     * 删除户型
     *
     * @param id
     * @return
     * @author:
     */
    R<Boolean> delete(String id);

}
