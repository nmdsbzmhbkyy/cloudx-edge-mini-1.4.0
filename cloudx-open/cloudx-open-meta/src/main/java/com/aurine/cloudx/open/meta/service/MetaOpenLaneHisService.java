package com.aurine.cloudx.open.meta.service;

import com.aurine.cloudx.open.origin.entity.ProjectOpenLaneHis;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-开关闸记录管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface MetaOpenLaneHisService {

    /**
     * 新增开关闸记录
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectOpenLaneHis> save(ProjectOpenLaneHis po);

    /**
     * 修改开关闸记录
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectOpenLaneHis> update(ProjectOpenLaneHis po);

    /**
     * 删除开关闸记录
     *
     * @param id
     * @return
     * @author:
     */
    R<Boolean> delete(String id);

}
