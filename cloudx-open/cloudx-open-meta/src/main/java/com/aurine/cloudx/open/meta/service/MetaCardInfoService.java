package com.aurine.cloudx.open.meta.service;

import com.aurine.cloudx.open.origin.entity.ProjectCard;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-卡信息管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface MetaCardInfoService {

    /**
     * 新增卡信息
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectCard> save(ProjectCard po);

    /**
     * 修改卡信息
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectCard> update(ProjectCard po);

    /**
     * 删除卡信息
     *
     * @param id
     * @return
     * @author:
     */
    R<Boolean> delete(String id);

}
