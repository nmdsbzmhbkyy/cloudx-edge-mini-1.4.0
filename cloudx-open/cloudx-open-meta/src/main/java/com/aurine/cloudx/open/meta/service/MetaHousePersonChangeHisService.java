package com.aurine.cloudx.open.meta.service;

import com.aurine.cloudx.open.origin.entity.ProjectHousePersonChangeHis;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-房屋人员变更日志
 *
 * @author: zouyu
 * @data: 2022/4/8 9:06
 */

public interface MetaHousePersonChangeHisService {

    /**
     * 新增房屋人员变更日志
     *
     * @param po
     * @return
     */
    R<ProjectHousePersonChangeHis> save(ProjectHousePersonChangeHis po);
}
