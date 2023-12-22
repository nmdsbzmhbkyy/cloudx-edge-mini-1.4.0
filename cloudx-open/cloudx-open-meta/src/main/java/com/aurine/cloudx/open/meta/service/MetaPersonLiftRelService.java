package com.aurine.cloudx.open.meta.service;

import com.aurine.cloudx.open.origin.entity.ProjectPersonLiftRel;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-人员电梯权限关系
 *
 * @author : zouyu
 * @date : 2022-07-28 10:19:13
 */
public interface MetaPersonLiftRelService {

    /**
     * 新增人员电梯权限关系
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectPersonLiftRel> save(ProjectPersonLiftRel po);

    /**
     * 修改人员电梯权限关系
     *
     * @param po
     * @return
     */
    R<ProjectPersonLiftRel> update(ProjectPersonLiftRel po);


    /**
     * 删除人员电梯权限关系
     *
     * @param id
     * @return
     */
    R<Boolean> delete(String id);
}
