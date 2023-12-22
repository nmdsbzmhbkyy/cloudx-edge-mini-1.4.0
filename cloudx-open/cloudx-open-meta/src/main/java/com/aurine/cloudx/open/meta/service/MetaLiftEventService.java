package com.aurine.cloudx.open.meta.service;

import com.aurine.cloudx.open.origin.entity.ProjectLiftEvent;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-设备关系管理
 *
 * @author : zouyu
 * @date : 2022-07-18 09:39:48
 */
public interface MetaLiftEventService {

    /**
     * 新增设备关系
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectLiftEvent> save(ProjectLiftEvent po);
}
