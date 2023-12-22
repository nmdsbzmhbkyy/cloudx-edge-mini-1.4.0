package com.aurine.cloudx.open.meta.service;

import com.aurine.cloudx.open.origin.entity.ProjectCardHis;
import com.aurine.cloudx.open.origin.entity.ProjectDeviceRel;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * 卡操作记录
 *
 * @author : zy
 * @date : 2022-11-14 11:25:20
 */

public interface MetaCardHisService {

    /**
     * 新增卡操作记录
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectCardHis> save(ProjectCardHis po);

    /**
     * 修改卡操作记录
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectCardHis> update(ProjectCardHis po);
}
