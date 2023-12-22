package com.aurine.cloudx.open.meta.service;

import com.aurine.cloudx.open.origin.entity.ProjectStaff;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-员工信息管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface MetaStaffInfoService {

    /**
     * 新增员工信息
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectStaff> save(ProjectStaff po);

    /**
     * 修改员工信息
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectStaff> update(ProjectStaff po);

    /**
     * 删除员工信息
     *
     * @param id
     * @return
     * @author:
     */
    R<Boolean> delete(String id);

}
