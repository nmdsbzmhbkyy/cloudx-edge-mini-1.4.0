package com.aurine.cloudx.open.meta.service;

import com.aurine.cloudx.open.origin.entity.ProjectHouseInfo;
import com.aurine.cloudx.open.origin.entity.ProjectSnapRecord;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-抓拍记录
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface MetaSnapRecordService {

    /**
     * 新增抓拍记录
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectSnapRecord> save(ProjectSnapRecord po);

    /**
     * 修改抓拍记录
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectSnapRecord> update(ProjectSnapRecord po);

    /**
     * 删除抓拍记录
     *
     * @param id
     * @return
     * @author:
     */
    R<Boolean> delete(String id);

}
